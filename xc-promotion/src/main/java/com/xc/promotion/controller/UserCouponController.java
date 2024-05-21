package com.xc.promotion.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.dto.CouponDiscountDTO;
import com.xc.promotion.domain.dto.OrderCouponDTO;
import com.xc.promotion.domain.dto.OrderProductDTO;
import com.xc.promotion.domain.query.UserCouponQuery;
import com.xc.promotion.domain.vo.CouponVO;
import com.xc.promotion.service.IDiscountService;
import com.xc.promotion.service.IUserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 用户优惠券接口
 *
 * @author Koi
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/user-coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final IUserCouponService userCouponService;

    private final IDiscountService discountService;

    /**
     *查询我的优惠券
     * @param query
     * @return
     */
    @GetMapping("/page")
    public PageDTO<CouponVO> queryMyCoupon(UserCouponQuery query){
        return userCouponService.queryMyCoupon(query);
    }

    /**
     * 领取优惠券
     * @param couponId
     */
    @PostMapping("/{couponId}/receive")
    public void receiveCoupon(@PathVariable("couponId") Long couponId){
        userCouponService.receiveCoupon(couponId);
    }

    /**
     * 兑换码兑换优惠券
     * @param code
     */
    @PostMapping("/{code}/exchange")
    public void exchangeCode(@PathVariable("code") String code){
        userCouponService.exchangeCode(code);
    }

    /**
     *查询我的优惠券的可用方案
     * @param orderProducts
     * @return
     */
    @PostMapping("/available")
    public List<CouponDiscountDTO> findDiscountSolution(@RequestBody List<OrderProductDTO> orderProducts){
        return discountService.findDiscountSolution(orderProducts);
    }

    /**
     * 根据券方案计算订单优惠明细
     * @param orderCouponDTO
     * @return
     */
    @PostMapping("/discount")
    public CouponDiscountDTO queryDiscountDetailByOrder(@RequestBody OrderCouponDTO orderCouponDTO){
        return discountService.queryDiscountDetailByOrder(orderCouponDTO);
    }

    /**
     * 核销指定优惠券
     * @param userCouponIds
     */
    @PutMapping("/use")
    public void writeOffCoupon(@RequestParam("couponIds") List<Long> userCouponIds){
        userCouponService.writeOffCoupon(userCouponIds);
    }

    /**
     * 退还指定优惠券
     * @param userCouponIds
     */
    @PutMapping("/refund")
    public void refundCoupon(@RequestParam("couponIds") List<Long> userCouponIds){
        userCouponService.refundCoupon(userCouponIds);
    }

    /**
     * 查询优惠券描述
     * @param userCouponIds
     * @return
     */
    @GetMapping("/rules")
    public List<String> queryDiscountRules(@RequestParam("couponIds") List<Long> userCouponIds){
        return userCouponService.queryDiscountRules(userCouponIds);
    }
}
