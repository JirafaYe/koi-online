package com.xc.promotion.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.UserCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.promotion.domain.query.UserCouponQuery;
import com.xc.promotion.domain.vo.CouponVO;

/**
 * <p>
 * 用户领取优惠券的记录，是真正使用的优惠券信息 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
public interface IUserCouponService extends IService<UserCoupon> {

    PageDTO<CouponVO> queryMyCoupon(UserCouponQuery query);

    void receiveCoupon(Long couponId);

    void checkAndCreateUserCoupon(Coupon coupon, Long userId, Integer serialNum);

    void exchangeCode(String code);
}
