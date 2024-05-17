package com.xc.user.controller;


import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
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
    public boolean login(@Valid @RequestBody UserRegisterReqVO vo) {
        return userBaseService.register(vo);
    }

    /**
     * 重置密码
     */
    @PostMapping("resetPwd")
    public boolean resetPwd(@RequestBody ResetPwdReqVO vo) {
        return userBaseService.resetPwd(vo);
    }


    /**
     * 手机号绑定
     */
    @PostMapping("bindMobile")
    public int bindMobile(@RequestBody @Valid BindMobileVO vo) {
        return userBaseService.bindMobile(vo);
    }

    /**
     * 分页展示用户信息
     */
    @PostMapping("listPageUser")
    PageDTO<UserInfoResVO> listPageUser(@RequestBody @Valid PageQuery vo){
        return userBaseService.listPageUser(vo);
    }


    /**
     * 获取多个用户信息
     */
    @GetMapping ("getUserInfos")
    public List<UserInfoResVO> getUserInfos(@RequestParam("ids")  List<Long> ids){
        return userBaseService.getUserInfos(ids);
    }

    /**
     * 管理员修改用户状态
     * @param vo
     * @return
     */
    @PostMapping("updateUserStatus")
    public boolean updateUserStatus(@RequestBody @Valid UpdateUserStatusVO vo){
        return userBaseService.updateUserStatus(vo);
    }

    /**
     * 设置用户默认地址
     */
    @PostMapping("setDefaultAddress/{id}")
    public Integer updateDefaultAddress(@RequestParam("id") Integer id ){
        return userBaseService.updateDefaultAddress(id);
    }

}
