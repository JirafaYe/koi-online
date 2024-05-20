package com.xc.promotion.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.query.UserCouponQuery;
import com.xc.promotion.domain.vo.CouponVO;
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
}
