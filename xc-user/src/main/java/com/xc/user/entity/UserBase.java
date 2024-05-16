package com.xc.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户基础信息表
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月13日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_base")
@ApiModel(value = "UserBase对象", description = "用户基础信息表")
public class UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id")
    private Long userId;

    @ApiModelProperty("1 管理员  2用户 ")
    @TableField("user_role")
    private Integer userRole;

    @ApiModelProperty("状态 0禁用 1启用")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty("账号")
    @TableField("account")
    private String account;

    @ApiModelProperty("昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("手机号(唯一)")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty("性别 0-female 1-male  2-未知")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("头像")
    @TableField("srcface")
    private String srcface;

    @ApiModelProperty("默认地址id ")
    @TableField("default_address")
    private Integer defaultAddress;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
