package com.xc.trade.entity.dto;

import com.xc.api.dto.promotion.CouponDiscountDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

 @Data
public class PreviewOrderDTO {
     /**
      * 购物车id列表
      */
     private List<Long> shoppingCharts;
     /**
     * 原始价格
     */
    private Double rawPrice;

    /**
     * 最终价格
     */
    private Double finalPrice;

    /**
     * 最佳折扣金额
     */
    private Double discountAmount;
     /**
      * 最佳优惠规则
      */
    private List<String> rules;

     /**
      * 最佳优惠券组合
      */
     private List<Long> coupons;

     /**
      * 可选优惠券
      */
     private List<CouponDiscountDTO> discounts;
}
