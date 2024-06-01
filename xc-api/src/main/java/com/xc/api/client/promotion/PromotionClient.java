package com.xc.api.client.promotion;

import com.xc.api.client.promotion.fallback.PromotionClientFallback;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "promotion-service", fallbackFactory = PromotionClientFallback.class)
public interface PromotionClient {

    @GetMapping("/coupon-scope/{id}")
    Boolean judgeCouponExist(@PathVariable("id") Long id);

    /**
     *查询我的优惠券的可用方案
     * @param orderProducts
     * @return
     */
    @PostMapping("/user-coupons/available")
    List<CouponDiscountDTO> findDiscountSolution(@RequestBody List<OrderProductDTO> orderProducts);

    /**
     * 根据券方案计算订单优惠明细
     * @param orderCouponDTO
     * @return
     */
    @PostMapping("/user-coupons/discount")
    CouponDiscountDTO queryDiscountDetailByOrder(@RequestBody OrderCouponDTO orderCouponDTO);

    /**
     * 核销指定优惠券
     * @param userCouponIds
     */
    @PutMapping("/user-coupons/use")
    void writeOffCoupon(@RequestParam("couponIds") List<Long> userCouponIds);

    /**
     * 退还指定优惠券
     * @param userCouponIds
     */
    @PutMapping("/user-coupons/refund")
    void refundCoupon(@RequestParam("couponIds") List<Long> userCouponIds);

    /**
     * 查询优惠券描述
     * @param userCouponIds
     * @return
     */
    @GetMapping("/user-coupons/rules")
    List<String> queryDiscountRules(@RequestParam("couponIds") List<Long> userCouponIds);

    /**
     * 内部api 获取优惠券待发布数量
     */
    @GetMapping("/coupons/getNum")
    Integer getNum();
}
