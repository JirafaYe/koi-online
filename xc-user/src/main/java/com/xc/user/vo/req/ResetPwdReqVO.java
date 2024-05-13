package com.xc.user.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPwdReqVO {

    @ApiModelProperty("新的密码")
    @NotNull(message = "新密码不能为空")
    private String password;

    @ApiModelProperty("验证码")
    @NotNull(message = "验证码不能为空")
    private String code;
}
