package com.xc.promotion.controller;


import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.service.ICouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 * 管理端优惠券接口
 *
 * @author Koi
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/coupons")
@Slf4j
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    /**
     * 新增优惠券
     * @param dto
     */
    @PostMapping
    public void saveCoupon(@Valid @RequestBody CouponFormDTO dto){
        couponService.saveCoupon(dto);
    }

}
