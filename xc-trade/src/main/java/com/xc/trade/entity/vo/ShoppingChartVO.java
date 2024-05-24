package com.xc.trade.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShoppingChartVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * sku
     */
    @NotNull
    private Long skuId;

    /**
     * 数量
     */
    @NotNull
    private Integer quantity;
}
