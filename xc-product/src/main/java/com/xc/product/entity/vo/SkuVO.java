package com.xc.product.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SkuVO implements Serializable {
    private Long id;

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
     * 价格
     */
    @NotNull
    private Double price;

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
