package com.xc.user.controller;


import com.xc.common.constants.JwtConstant;
import com.xc.common.utils.UserContext;
import com.xc.user.service.UserBaseService;
import com.xc.user.vo.req.ResetPwdReqVO;
import com.xc.user.vo.req.UserLoginReqVO;
import com.xc.user.vo.req.UserRegisterReqVO;
import com.xc.user.vo.res.UserLoginResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    public UserLoginResVO login(@Valid @RequestBody UserLoginReqVO vo){
        return userBaseService.login(vo);
    }
    /**
     * 发送验证码
     */
    @PostMapping("sendcode/{phone}")
    public void sendCode(@Valid @RequestParam("phone")  String phone){
        userBaseService.sendCode(phone);
    }
    /**
     * 注册
     */
    @PostMapping("register")
    public boolean login(@Valid @RequestBody UserRegisterReqVO vo){
        return userBaseService.register(vo);
    }
    /**
     * 重置密码
     */
    @PostMapping("resetPwd")
    public boolean resetPwd(@RequestBody ResetPwdReqVO vo) {
      return userBaseService.resetPwd(vo);
    }



}

