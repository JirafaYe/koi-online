package com.xc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.user.entity.UserBase;
import com.xc.user.vo.req.BindMobileVO;
import com.xc.user.vo.req.ResetPwdReqVO;
import com.xc.user.vo.req.UserLoginReqVO;
import com.xc.user.vo.req.UserRegisterReqVO;
import com.xc.user.vo.res.UserLoginResVO;

import java.util.List;

/**
 * <p>
 * 用户基础信息表 服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月11日
 */
public interface UserBaseService extends IService<UserBase> {
    /**
     * 用户登录
     * @param vo
     * @return
     */
    UserLoginResVO login(UserLoginReqVO vo);

    /**
     * 发送手机验证码
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 用户注册
     * @param vo
     * @return
     */
    boolean register(UserRegisterReqVO vo);

    /**
     * 重置密码
     * @param vo
     * @return
     */
    boolean resetPwd(ResetPwdReqVO vo);

    /**
     * 获取多个用户信息
     * @param vo
     * @return
     */
    List<UserInfoResVO> getUserInfos(LongIdsVO vo);

    /**
     * 分页查询用户
     * @param vo
     * @return
     */
    PageDTO<UserInfoResVO> listPageUser(PageQuery vo);

    /**
     * 修改用户状态
     * @param vo
     * @return
     */
    boolean updateUserStatus(LongIdsVO vo);

    /**
     * 绑定手机号
     *
     * @param vo
     * @return
     */
    int bindMobile(BindMobileVO vo);
}
