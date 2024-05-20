package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SkuVO implements Serializable {
    /**
     * 所属spu id
     */
    @NotNull
    private Long spuId;

    /**
     * 单个id
     */
    @NotNull
    private Long imageId;

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

    private Boolean available;
}
