package com.xc.trade.entity.dto;

import lombok.Data;

@Data
public class OrderDetailsDTO {
    /**
     * detailsId
     */
    private Long id;

    /**
     * spu名字
     */
    private String spuName;

    /**
     * spuId
     */
    private Long spuId;

    /**
     * 详细sku id
     */
    private Long skuId;

    /**
     * sku属性
     */
    private String attributes;

    /**
     * sku图片url
     */
    private String image;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 金额*100
     */
    private Integer price;

    /**
     * 优惠后金额*100
     */
    private Integer finalPrice;

    /**
     * 不为空则退款，退款申请中0，退款申请成功1，已退款2
     */
    private Integer refundStatus;

    /**
     * 是否取消，默认false
     */
    private boolean canceled;
}
