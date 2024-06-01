package com.xc.trade.entity.vo;

import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderVO {
    /**
     * 购物车id list
     */
    @NotNull
    private List<Long> shoppingCharts;

    /**
     * 地址id
     */
    @NotNull
    private Long addressId;

    /**
     * 优惠券选择
     */
    @Nullable
    private List<Long> coupons;
}
