package com.xc.user.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BindMobileVO {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
