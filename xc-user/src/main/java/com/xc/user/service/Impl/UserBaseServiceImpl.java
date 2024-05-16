package com.xc.user.service.Impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.log.LogClient;
import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.constants.JwtConstant;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.CommonException;
import com.xc.common.exceptions.UnauthorizedException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.JwtTokenUtils;
import com.xc.common.utils.UserContext;
import com.xc.user.entity.UserBase;
import com.xc.user.enums.UserConstants;
import com.xc.user.mapper.UserBaseMapper;
import com.xc.user.service.UserBaseService;
import com.xc.user.utils.IdGeneratorSnowflake;
import com.xc.user.utils.MD5Utils;
import com.xc.user.utils.RandomStringGenerator;
import com.xc.user.vo.req.BindMobileVO;
import com.xc.user.vo.req.ResetPwdReqVO;
import com.xc.user.vo.req.UserLoginReqVO;
import com.xc.user.vo.req.UserRegisterReqVO;
import com.xc.user.vo.res.UserLoginResVO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xc.common.constants.ErrorInfo.Msg.*;

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

    private static   String PHONE_CODE_KEY ="";

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserBaseMapper userBaseMapper;

    @Resource
    private LogClient logClient;

    @Override
    public UserLoginResVO login( UserLoginReqVO vo) {

        Integer type= vo.getLoginType();
//        1.账号密码登录
        UserBase user = null;
        if(LOGIN_TYPE_USER.equals(type)){
            String account = vo.getAccount();
            String password = vo.getPassword();
            if(StringUtils.isAnyBlank(account,password)){
                throw new CommonException("账号或密码为空");
            }
            //加密
            String newPw = MD5Utils.inputPassToFormPass(password);
            LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<UserBase>().eq(UserBase::getAccount, account)
                    .eq(UserBase::getPassword, newPw);
             user = userBaseMapper.selectOne(lqw);
            if(user == null){
                throw new CommonException(USER_NOT_EXISTS);
            }

        }else if (LOGIN_TYPE_PHONE.equals(type)){
          //手机号验证码登录
            String phone = vo.getPhone();
            if(phone == null){
                throw new CommonException("手机号不能为空");
            }
            //redis 中获取验证码
            String o = (String)redisTemplate.opsForValue().get(PHONE_CODE_KEY);
            if(!vo.getCode().equals(o)){
                throw  new CommonException(INVALID_VERIFY_CODE);
            }
            LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<UserBase>().eq(UserBase::getMobile, phone);
            user = userBaseMapper.selectOne(lqw);
            if(user == null){
                throw new CommonException(USER_NOT_EXISTS);
            }
        }
        if(user.getStatus().equals(0)){
            throw new UnauthorizedException("无权限");
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtConstant.USER_ID,user.getUserId());
//        2、登录成功，生成JWT返回
        String token = JwtTokenUtils.createJWT(claims);

        UserLoginResVO resVO = new UserLoginResVO();
        BeanUtils.copyProperties(user,resVO);
        resVO.setToken(token);
        return resVO;
    }

      /**
     * 发送短信验证码
       * TODO
     */
      @Override
    public void sendCode(String phone) {
//          DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
//                  "1这里填入阿里云用户的AccessKey ID", "2这里填入阿里云用户的AccessKey Secre");
//          IAcsClient client = new DefaultAcsClient(profile);
//
//          CommonRequest request = new CommonRequest();
//          request.setMethod(MethodType.POST);
//          request.setDomain("dysmsapi.aliyuncs.com");
//          request.setVersion("2017-05-25");
//          request.setAction("SendSms");
//          request.putQueryParameter("PhoneNumbers", phone);
//          request.putQueryParameter("SignName", "4短信签名");
//          request.putQueryParameter("TemplateCode", "5短信模版CODE");
//
//          /*生成随机4位验证码*/
//          String code = UUID.randomUUID().toString().substring(0, 4);
//          Map<String, Object> map = new HashMap<>();
//          map.put("code", code);
//          request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
//
//          try {
//              CommonResponse response = client.getCommonResponse(request);
//              System.out.println(response.getData());
//          } catch (ServerException e) {
//              e.printStackTrace();
//          } catch (ClientException e) {
//              e.printStackTrace();
//          }

//          模拟验证码
          String code = RandomUtil.randomString(4);
          System.out.println("验证码是"+code);
          PHONE_CODE_KEY = phone;
          //保存验证码(60s失效）
          redisTemplate.opsForValue().set(PHONE_CODE_KEY,code,60, TimeUnit.SECONDS);
      }

    @Override
    public boolean register(UserRegisterReqVO vo) {
        String account = vo.getAccount(); //账号唯一
        String password = vo.getPassword();
        if(StringUtils.isAnyBlank(account,password)){
            throw new CommonException("账号或者密码不能为空");
        }
        LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserBase::getAccount,account);
        UserBase user = userBaseMapper.selectOne(lqw);
        if(user != null){
            throw new CommonException("账号不能重复");
        }
        //密码加密
        String newPass = MD5Utils.inputPassToFormPass(password);
        UserBase userBase = new UserBase();
        userBase.setUserId(IdGeneratorSnowflake.snowflakeId());
        userBase.setAccount(account);
        userBase.setPassword(newPass);
        userBase.setSrcface(UserConstants.deSrcUrl);
        userBase.setNickName(RandomStringGenerator.usingRandom(6));
        return this.save(userBase);

    }

    @Override
    public boolean resetPwd(ResetPwdReqVO vo) {
          //校验用户
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new CommonException(USER_NOT_EXISTS);
        }
        UserBase user = userBaseMapper.selectById(userId);
        //加密匹配密码是否正确
        if(!user.getPassword().equals(MD5Utils.inputPassToFormPass(vo.getOldPassword()))){
            throw new CommonException(PASSWORD_ERROR);
        }

        //更新新密码
        String password = MD5Utils.inputPassToFormPass(vo.getPassword());
        boolean b = userBaseMapper.updatePassword(password,userId);
        return false;

    }

    @Override
    public List<UserInfoResVO> getUserInfos(LongIdsVO vo) {
        List<UserBase> infos = userBaseMapper.selectBatchIds(vo.getIds());
        List<UserInfoResVO> list = new ArrayList<>();
        for (UserBase info : infos) {
            UserInfoResVO userInfoResVO = new UserInfoResVO();
            BeanUtils.copyProperties(info,userInfoResVO);
            list.add(userInfoResVO);
        }

        return list;
    }

    @Override
    public PageDTO<UserInfoResVO> listPageUser(PageQuery vo) {
        Page<UserBase> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        LambdaQueryWrapper<UserBase> lqw = new LambdaQueryWrapper<UserBase>();
        Page<UserBase> pageInfos = userBaseMapper.selectPage(page, lqw);
        List<UserInfoResVO> loginVO = getUserInfoVO(pageInfos.getRecords());
        PageDTO<UserInfoResVO> ans = new PageDTO<>();
        ans.setList(loginVO);
        ans.setTotal(pageInfos.getTotal());
        return ans;
    }

    @Override
    public boolean updateUserStatus(LongIdsVO vo) {
          return userBaseMapper.updateUserStatus(vo.getIds());

    }

    @Override
    public int bindMobile(BindMobileVO vo) {
        redisTemplate.opsForValue().set(PHONE_CODE_KEY,vo.getCode());
        String code = (String)redisTemplate.opsForValue().get(PHONE_CODE_KEY);
        if(!vo.getCode().equals(code)){
            throw new CommonException(INVALID_VERIFY_CODE);
        }
        Long user = UserContext.getUser();
        UserBase userBase = userBaseMapper.selectById(user);
        UserBase newUser = new UserBase();
        BeanUtils.copyProperties(userBase,newUser);
        userBase.setMobile(vo.getMobile());
        return userBaseMapper.updateById(userBase) ;

    }

    private List<UserInfoResVO> getUserInfoVO(List<UserBase> records){
         List<UserInfoResVO> voList = new ArrayList<>();
         for (UserBase record : records) {
             UserInfoResVO userInfoResVO = new UserInfoResVO();
             BeanUtils.copyProperties(record,userInfoResVO);
             voList.add(userInfoResVO);
         }
         return voList;
     }


}
