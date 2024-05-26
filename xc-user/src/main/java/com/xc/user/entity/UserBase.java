package com.xc.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id")
    private Long userId;

    @TableField("user_role")
    private Integer userRole;


    @TableField("status")
    private Integer status;


    @TableField("account")
    private String account;

    @TableField("nick_name")
    private String nickName;

    @TableField("password")
    private String password;

    @TableField("mobile")
    private String mobile;


    @TableField("gender")
    private Integer gender;


    @TableField("email")
    private String email;


    @TableField("srcface")
    private String srcface;


    @TableField("default_address")
    private Integer defaultAddress;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
