package com.xc.product.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class SkuPageVO {
    private Long id;

    /**
     * 所属spu id
     */
    private Long spuId;

    /**
     * 所属分类 id
     */
    private Long categoryId;

    /**
     * 所属spu name
     */
    private String  spuName;

    /**
     * 图片
     */
    private String image;

    private Long imageId;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 库存, 上架时同时更新spu库存
     */
    private Integer num;

    /**
     * json储存的特征描述
     */
    private String attributes;

    private Boolean available;

    private String createrName;
}
