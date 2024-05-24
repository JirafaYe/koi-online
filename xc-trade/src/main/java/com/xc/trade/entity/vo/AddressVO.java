package com.xc.trade.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressVO {
    /**
     * 主键，update时传
     */
    private Long id;

    /**
     * 收货人
     */
    @NotBlank
    private String receiverName;

    /**
     * 电话号
     */
    @NotBlank
    private String phone;

    /**
     * 省
     */
    @NotBlank
    private String province;

    /**
     * 市
     */
    @NotBlank
    private String city;

    /**
     * 区
     */
    @NotBlank
    private String district;

    /**
     * 详细地址
     */
    @NotBlank
    private String details;
}
