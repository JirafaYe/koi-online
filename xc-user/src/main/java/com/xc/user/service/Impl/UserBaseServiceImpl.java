package com.xc.user.service.Impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.constants.JwtConstant;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.exceptions.UnauthorizedException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.JwtTokenUtils;
import com.xc.common.utils.UserContext;
import com.xc.user.entity.UserBase;
import com.xc.user.enums.UserConstants;
import com.xc.user.mapper.UserBaseMapper;
import com.xc.user.service.UserBaseService;
import com.xc.user.utils.IdGeneratorSnowflake;
import com.xc.user.utils.MD5Utils;
import com.xc.user.utils.RandomStringGenerator;
import com.xc.user.vo.req.*;
import com.xc.user.vo.req.res.UserLoginResVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static com.xc.common.constants.ErrorInfo.Msg.*;
import static com.xc.common.constants.JwtConstant.JWT_BLANK_LIST;

/**
 * <p>
 * 用户基础信息表 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月11日
 */
@Service
public class UserBaseServiceImpl extends ServiceImpl<UserBaseMapper, UserBase> implements UserBaseService {
    /**
     * 用户账号密码登录
     */
    private static final Integer LOGIN_TYPE_USER = 1;
    /**
     * 短信验证码登录
     */
    private static final Integer LOGIN_TYPE_PHONE = 2;

    private static String PHONE_CODE_KEY = "";

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserBaseMapper userBaseMapper;


    @Override
    public UserLoginResVO login(UserLoginReqVO vo) {
        UserBase user = null;
        String account = vo.getAccount();
        String password = vo.getPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new CommonException("账号或密码为空");
        }
        //加密
        String newPw = MD5Utils.inputPassToFormPass(password);
        LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<UserBase>().eq(UserBase::getAccount, account)
                .eq(UserBase::getPassword, newPw);
        user = userBaseMapper.selectOne(lqw);
        if (user == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        if (user.getStatus().equals(0)) {
            throw new UnauthorizedException("无权限");
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtConstant.USER_ID, user.getUserId());
//        2、登录成功，生成JWT返回
        String token = JwtTokenUtils.createJWT(claims);
        UserLoginResVO resVO = new UserLoginResVO();
        BeanUtils.copyProperties(user, resVO);
        resVO.setToken(token);
        return resVO;
    }

    /**
     * 发送短信验证码
     * TODO
     */
    @Override
    public void sendCode(String phone) {
//          模拟验证码
        String code = RandomUtil.randomString(4);
        System.out.println("验证码是" + code);
        PHONE_CODE_KEY = phone;
        //保存验证码(60s失效）
        stringRedisTemplate.opsForValue().set(PHONE_CODE_KEY, code, 60, TimeUnit.SECONDS);
    }

    @Override
    public boolean register(UserRegisterReqVO vo) {
        String account = vo.getAccount(); //账号唯一
        String password = vo.getPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new CommonException("账号或者密码不能为空");
        }
        LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserBase::getAccount, account);
        UserBase user = userBaseMapper.selectOne(lqw);
        if (user != null) {
            throw new CommonException("账号不能重复");
        }
        //密码加密
        String newPass = MD5Utils.inputPassToFormPass(password);
        UserBase userBase = new UserBase();
        //id生成
        IdGeneratorSnowflake idWorker = new IdGeneratorSnowflake(1);
        userBase.setUserId(idWorker.nextId());
        userBase.setAccount(account);
        userBase.setPassword(newPass);
        userBase.setSrcface(UserConstants.deSrcUrl);
        userBase.setNickName(RandomStringGenerator.usingRandom(6));
        return this.save(userBase);

    }

    @Override
    public Boolean resetPwd(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return null;
        }
        userBaseMapper.selectBatchIds(ids).stream().map(userBase -> {
            userBase.setPassword(MD5Utils.inputPassToFormPass("123456"));
            return userBase;
        }).forEach(userBaseMapper::updateById);
        return true;
    }

    @Override
    public Boolean updatePwd(ResetPwdReqVO vo) {
        //校验用户
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        UserBase user = userBaseMapper.selectById(userId);
        //加密匹配密码是否正确
        if (!user.getPassword().equals(MD5Utils.inputPassToFormPass(vo.getOldPassword()))) {
            throw new CommonException(PASSWORD_ERROR);
        }
        //更新新密码
        String password = MD5Utils.inputPassToFormPass(vo.getPassword());
        return userBaseMapper.updatePassword(password, userId);
    }

    @Override
    public Integer updateUserInfo(UpdateUserInfoVO vo) {
        Long user = UserContext.getUser();
        if (user == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        UserBase userBase = userBaseMapper.selectById(user);
        if (userBase == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        userBase.setNickName(vo.getNickName());
        userBase.setSrcface(vo.getSrcface());
        userBase.setGender(vo.getGender());
        return userBaseMapper.updateById(userBase);

    }

    @Override
    public UserInfoResVO getUserInfo() {
        Long user = UserContext.getUser();
        if (user == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        UserBase userBase = userBaseMapper.selectById(user);
        if (userBase == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        UserInfoResVO ans = new UserInfoResVO();
        BeanUtils.copyProperties(userBase, ans);
        return ans;
    }


    @Override
    public List<UserInfoResVO> getUserInfos(List<Long> ids) {
        if (!CollUtil.isEmpty(ids)) {
            List<UserBase> infos = userBaseMapper.selectBatchIds(ids);
            List<UserInfoResVO> list = new ArrayList<>();
            for (UserBase info : infos) {
                UserInfoResVO userInfoResVO = new UserInfoResVO();
                BeanUtils.copyProperties(info, userInfoResVO);
                list.add(userInfoResVO);
            }
            return list;
        }
        return null;
    }

    @Override
    public PageDTO<UserInfoResVO> listPageUser(SearchUserVO vo) {
        Page<UserBase> page = lambdaQuery().like(StringUtils.isNotEmpty(vo.getNickName()), UserBase::getNickName, vo.getNickName())
                .like(StringUtils.isNotEmpty(vo.getAccount()), UserBase::getAccount, vo.getAccount())
                .like(StringUtils.isNotEmpty(vo.getMobile()), UserBase::getMobile, vo.getMobile())
                .eq(vo.getStatus() != null, UserBase::getStatus, vo.getStatus())
                .page(vo.toMpPage());
        List<UserBase> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }
        List<UserInfoResVO> loginVO = getUserInfoVO(records);
        return PageDTO.of(page, loginVO);
    }

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
    }

    @Override
    public int updateUserStatus(UpdateUserStatusVO vo) {
        UserBase userBase = userBaseMapper.selectById(vo.getId());
        if (userBase == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        //禁用: 把用户放入黑名单
        if (vo.getStatus().equals(0)) {
            stringRedisTemplate.opsForSet().add(JWT_BLANK_LIST, String.valueOf(vo.getId()));

        } else {
            stringRedisTemplate.opsForSet().remove(JWT_BLANK_LIST, String.valueOf(vo.getId()));
        }
        return userBaseMapper.updateById(userBase.setStatus(vo.getStatus()));


    }

    @Override
    public int bindMobile(BindMobileVO vo) {
        stringRedisTemplate.opsForValue().set(PHONE_CODE_KEY, vo.getCode());
        String code = stringRedisTemplate.opsForValue().get(PHONE_CODE_KEY);
//        if (!vo.getCode().equals(code)) {
//            throw new CommonException(INVALID_VERIFY_CODE);
//        }
        //模拟验证码
        if (!vo.getCode().equals("1234")) {
            throw new CommonException(INVALID_VERIFY_CODE);
        }
        Long user = UserContext.getUser();
        UserBase userBase = userBaseMapper.selectById(user);
        userBase.setMobile(vo.getMobile());
        return userBaseMapper.updateById(userBase);

    }

    @Override
    public int updateDefaultAddress(Integer id) {
        Long user = UserContext.getUser();
        UserBase userInfo = userBaseMapper.selectById(user);
        userInfo.setDefaultAddress(id);
        return userBaseMapper.updateById(userInfo);
    }


    private List<UserInfoResVO> getUserInfoVO(List<UserBase> records) {
        List<UserInfoResVO> voList = new ArrayList<>();
        voList = records.stream()
                .map(record -> {
                    UserInfoResVO userInfoResVO = BeanUtils.copyBean(record, UserInfoResVO.class);
                    userInfoResVO.setMobile(mobileEncrypt(record.getMobile()));
                    return userInfoResVO;
                })
                .collect(Collectors.toList());
        return voList;
    }

    @Override
    public List<UserInfoResVO> getUserListByName(String name) {
        LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<UserBase>()
                .like(UserBase::getAccount, name);  //
        return userBaseMapper.selectList(lqw).stream().map(userBase -> {
            UserInfoResVO userInfoResVO = new UserInfoResVO();
            BeanUtils.copyProperties(userBase, userInfoResVO);
            return userInfoResVO;
        }).collect(Collectors.toList());
    }

}
