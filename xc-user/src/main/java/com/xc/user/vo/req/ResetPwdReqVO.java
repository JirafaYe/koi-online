package com.xc.user.vo.req;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPwdReqVO {

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String password;

    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
}
