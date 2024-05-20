package com.xc.promotion.mapper;

import com.xc.promotion.domain.enums.UserCouponStatus;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.UserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户领取优惠券的记录，是真正使用的优惠券信息 Mapper 接口
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    List<Coupon> queryMyCoupons(@Param("userId") Long userId);

    List<Coupon> queryCouponByUserCouponIds(@Param("userCouponIds") List<Long> userCouponIds, @Param("status") UserCouponStatus status);
}
