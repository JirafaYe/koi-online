package com.xc.user.vo.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录参数")
public class UserInfoResVO {
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("1 管理员  2用户 ")
    private Integer userRole;

    @ApiModelProperty("手机号(唯一)")
    private String mobile;

    @ApiModelProperty("状态 0禁用 1启用")
    private Integer status;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别 0-female 1-male")
    private Integer gender;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String srcface;


    @ApiModelProperty("默认地址id")
    private Integer defaultAdress;

    @ApiModelProperty("token")
    private String token;
}
