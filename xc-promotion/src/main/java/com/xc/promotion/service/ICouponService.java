package com.xc.promotion.service;

import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.domain.po.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券的规则信息 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
public interface ICouponService extends IService<Coupon> {

    void saveCoupon(CouponFormDTO dto);
}
