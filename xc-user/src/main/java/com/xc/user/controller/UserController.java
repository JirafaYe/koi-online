package com.xc.user.controller;


import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.log.aspect.Log;
import com.xc.user.service.UserBaseService;
import com.xc.user.vo.req.*;
import com.xc.user.vo.res.UserLoginResVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户基础信息表 前端控制器
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月11日
 */

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserBaseService userBaseService;

    /**
     * 登录
     */
    @PostMapping("login")
    @Log(title = "用户登录")
    public UserLoginResVO login(@Valid @RequestBody UserLoginReqVO vo) {
        return userBaseService.login(vo);
    }

    /**
     * 发送验证码
     */
    @PostMapping("sendcode/{phone}")
    public void sendCode(@Valid @RequestParam("phone") String phone) {
        userBaseService.sendCode(phone);
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public boolean register(@Valid @RequestBody UserRegisterReqVO vo) {
        return userBaseService.register(vo);
    }

    /**
     * 管理员重置密码
     */
    @PostMapping("resetPwd")
    public Boolean resetPwd(@RequestParam("ids") List<Long> ids) {
        return userBaseService.resetPwd(ids);
    }

    /**
     * 用户修改密码
     */
    @PostMapping("updatePwd")
    public Boolean updatePwd(@RequestBody ResetPwdReqVO vo) {
        return userBaseService.updatePwd(vo);
    }

    /**
     * 手机号绑定
     */
    @PostMapping("bindMobile")
    public int bindMobile(@RequestBody @Valid BindMobileVO vo) {
        return userBaseService.bindMobile(vo);
    }

    /**
     * 分页查询用户信息
     */
    @GetMapping("listPageUser")
    PageDTO<UserInfoResVO> listPageUser(SearchUserVO vo) {
        return userBaseService.listPageUser(vo);
    }


    /**
     * 获取多个用户信息
     */
    @GetMapping("getUserInfos")
    public List<UserInfoResVO> getUserInfos(@RequestParam("ids") List<Long> ids) {
        return userBaseService.getUserInfos(ids);
    }

    /**
     * 管理员修改用户状态
     *
     * @param vo
     * @return
     */
    @PostMapping("updateUserStatus")
    public Integer updateUserStatus(@RequestBody @Valid UpdateUserStatusVO vo) {
        return userBaseService.updateUserStatus(vo);
    }

    /**
     * 设置用户默认地址
     *
     * @param addressId 要设为默认的地址ID
     */
    @PostMapping("setDefaultAddress")
    public Integer updateDefaultAddress(@RequestParam("addressId") Integer addressId) {
        return userBaseService.updateDefaultAddress(addressId);
    }

    /**
     * 更新用户信息  TODO
     * @param vo
     * @return
     */
    @PostMapping("updateUserInfo")
    public Integer updateUserInfo(@RequestBody UpdateUserInfoVO vo) {
        return userBaseService.updateUserInfo(vo);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("getUserInfo")
    public UserInfoResVO getUserInfo(){
        return userBaseService.getUserInfo();
    }

}
