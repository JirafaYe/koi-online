package com.xc.user.vo.res;


import lombok.Data;


/**
 * 用户登录参数
 */
@Data
public class UserLoginResVO {
    /**
     * id
     */
    private Long userId;
    /**
     * 1 管理员  2用户
     */
    private Integer userRole;
    /**
     * 手机号(唯一)
     */
    private String mobile;
    /**
     * 状态 0禁用 1启用
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
     * 性别 0-female 1-male
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
     * 默认地址id
     */
    private Integer defaultAddress;

    /**
     * token
     */
    private String token;
}
