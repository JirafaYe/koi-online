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

}
