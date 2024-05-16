package com.xc.api.dto.user.res;

import lombok.Data;

@Data

public class UserInfoResVO {
    /**
     * id
     */
    private Long userId;
    /**
     * 角色
     */
    private Integer userRole;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 账号
     */
    private String account;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String srcface;
    /**
     * 用户默认收获地址
     */
    private Integer defaultAddress;

}
