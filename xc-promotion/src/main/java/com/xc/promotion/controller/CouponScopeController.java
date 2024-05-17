package com.xc.promotion.controller;


import com.xc.promotion.service.ICouponScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 优惠券可用范围
 *
 * @author Koi
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/coupon-scope")
@RequiredArgsConstructor
public class CouponScopeController {

    private final ICouponScopeService scopeService;

    /**
     * 判断改分类下是否有优惠券
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Boolean judgeCouponExist(@PathVariable("id") Long id){
        return scopeService.judgeCouponExist(id);
    }
}
