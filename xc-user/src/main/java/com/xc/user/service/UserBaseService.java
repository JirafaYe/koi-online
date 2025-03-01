package com.xc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.user.entity.UserBase;
import com.xc.user.vo.req.*;
import com.xc.user.vo.req.res.UserLoginResVO;

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
     *
     * @param vo
     * @return
     */
    UserLoginResVO login(UserLoginReqVO vo);

    /**
     * 发送手机验证码
     *
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 用户注册
     *
     * @param vo
     * @return
     */
    boolean register(UserRegisterReqVO vo);

    /**
     * 重置密码
     *
     * @param ids
     * @return
     */
    Boolean resetPwd(List<Long> ids);

    /**
     * 获取多个用户信息
     *
     * @param ids
     * @return
     */
    List<UserInfoResVO> getUserInfos(List<Long> ids);

    /**
     * 分页查询用户
     *
     * @param vo
     * @return
     */
    PageDTO<UserInfoResVO> listPageUser(SearchUserVO vo);

    /**
     * 修改用户状态
     *
     * @param vo
     * @return
     */
    int updateUserStatus(UpdateUserStatusVO vo);

    /**
     * 绑定手机号
     *
     * @param vo
     * @return
     */
    int bindMobile(BindMobileVO vo);

    /**
     * 设置用户默认地址
     *
     * @param id
     * @return
     */
    int updateDefaultAddress(Integer id);

    /**
     * 修改密码
     *
     * @param vo
     * @return
     */
    Boolean updatePwd(ResetPwdReqVO vo);

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    Integer updateUserInfo(UpdateUserInfoVO vo);

    /**
     * 获取用户信息
     *
     * @return
     */
    UserInfoResVO getUserInfo();

    List<UserInfoResVO> getUserListByName(String name);
}
