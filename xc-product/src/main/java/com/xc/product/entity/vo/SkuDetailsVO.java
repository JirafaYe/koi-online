package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SkuDetailsVO {
    /**
     * 单个id
     */
    @NotNull
    private Long imageId;

    /**
     * 价格
     */
    @NotNull
    private Integer price;

    /**
     * 库存, 上架时同时更新spu库存
     */
    @NotNull
    private Long num;

    /**
     * json储存的特征描述
     */
    @NotBlank
    private String attributes;

    /**
     * 默认false
     */
    private Boolean available;
}
