package com.xc.user.vo.req;

import lombok.Data;
//修改用户信息参数
@Data
public class UpdateUserInfoVO {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别 0-female 1-male
     */
    private Integer gender;

    /**
     * 头像
     */
    private String srcface;
}
