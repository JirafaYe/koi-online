package com.xc.user.vo.req;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户登录请求参数
 */
@Data
public class UserLoginReqVO {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 登录类型 1账号 2手机号
     */
    @NotNull(message = "类型不能为空")
    private Integer loginType;
}
