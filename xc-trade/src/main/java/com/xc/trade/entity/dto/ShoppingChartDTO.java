package com.xc.trade.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShoppingChartDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * spu
     */
    private Long spuId;

    /**
     * spu name
     */
    private String  spuName;

    /**
     * sku
     */
    private Long skuId;

    /**
     * 图片
     */
    private String image;

    /**
     * json储存的特征描述
     */
    private String attributes;

    /**
     * 价格，两位小数
     */
    private double price;

    /**
     * 是否有效（下架无效）
     */
    private boolean available;

    /**
     * 数量
     */
    private Integer quantity;
}
