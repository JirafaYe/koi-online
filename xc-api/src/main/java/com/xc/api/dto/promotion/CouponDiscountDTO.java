package com.xc.api.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单的可用优惠券及折扣信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CouponDiscountDTO {

    /**
     * 用户优惠券id集合
     */
    private List<Long> ids = new ArrayList<>();

    /**
     * 优惠券规则
     */
    private List<String> rules = new ArrayList<>();

    /**
     * 本订单最大优惠金额
     */
    private Integer discountAmount = 0;

    /**
     * 优惠明细，key是商品id，value是商品优惠金额
     */
    private Map<Long, Integer> discountDetail;
}