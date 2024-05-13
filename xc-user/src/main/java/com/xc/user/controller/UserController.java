package com.xc.user.controller;


import com.xc.user.service.UserBaseService;
import com.xc.user.vo.req.ResetPwdReqVO;
import com.xc.user.vo.req.UserLoginReqVO;
import com.xc.user.vo.req.UserRegisterReqVO;
import com.xc.user.vo.res.UserLoginResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 用户基础信息表 前端控制器
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月11日
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

    @Resource
    private UserBaseService userBaseService;

    @ApiOperation("登录")
    @PostMapping("login")
    public UserLoginResVO login(@Valid @RequestBody UserLoginReqVO vo){
        return userBaseService.login(vo);
    }

    @ApiOperation("发送验证码")
    @PostMapping("sendcode/{phone}")
    public void sendCode(@Valid @RequestParam("phone")  String phone){
        userBaseService.sendCode(phone);
    }

    @ApiOperation("注册")
    @PostMapping("register")
    public boolean login(@Valid @RequestBody UserRegisterReqVO vo){
        return userBaseService.register(vo);
    }

    @ApiOperation("重置密码")
    @PostMapping("resetPwd")
    public boolean resetPwd(@RequestBody ResetPwdReqVO vo) {
      return userBaseService.resetPwd(vo);
    }
}

