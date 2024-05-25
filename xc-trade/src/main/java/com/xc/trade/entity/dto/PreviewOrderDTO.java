package com.xc.trade.entity.dto;

import com.xc.api.dto.promotion.CouponDiscountDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

 @Data
public class PreviewOrderDTO {
     /**
     * 原始价格
     */
    private Double raw_price;

    /**
     * 最终价格
     */
    private Double final_price;

    /**
     * 最佳折扣金额
     */
    private Double discountAmount;
     /**
      * 最佳优惠规则
      */
    private List<String> rules;

     /**
      * 可选优惠券
      */
     private List<CouponDiscountDTO> discounts;
}
