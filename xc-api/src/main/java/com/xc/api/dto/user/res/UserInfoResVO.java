package com.xc.api.dto.user.res;

import lombok.Data;

@Data

public class UserInfoResVO {

    private Long id;


    private Integer userRole;


    private String mobile;


    private Integer status;


    private String account;


    private String nickName;


    private Integer gender;


    private String email;


    private String srcface;



    private Integer defaultAdress;


    private String token;
}
