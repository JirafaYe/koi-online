package com.xc.product.entity.vo;

import lombok.Data;


@Data
public class SkuPageVO {
    private Long id;

    /**
     * 所属spu id
     */
    private Long spuId;

    /**
     * 图片
     */
    private String image;

    /**
     * 库存, 上架时同时更新spu库存
     */
    private Long num;

    /**
     * json储存的特征描述
     */
    private String attributes;

    private Boolean available;

    private String createrName;
}
