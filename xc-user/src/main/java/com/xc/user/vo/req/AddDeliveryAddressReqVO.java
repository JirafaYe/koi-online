package com.xc.user.vo.req;


import lombok.Data;

@Data
public class AddDeliveryAddressReqVO {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收件人姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 所在地区
     */
    private String area;

    /**
     * 详细地址
     */
    private String adress;

}
