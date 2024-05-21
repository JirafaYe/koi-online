package com.xc.promotion.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.domain.dto.CouponIssueFormDTO;
import com.xc.promotion.domain.po.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.promotion.domain.query.CouponQuery;
import com.xc.promotion.domain.vo.CouponDetailVo;
import com.xc.promotion.domain.vo.CouponPageVO;
import com.xc.promotion.domain.vo.CouponVO;

import java.util.List;

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

    PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query);

    void beginIssue(CouponIssueFormDTO dto);

    CouponDetailVo queryCouponById(Long id);

    void deleteById(Long id);

    void updateCoupon(CouponFormDTO dto);

    void pauseIssue(Long id);

    List<CouponVO> queryIssuingCoupon();

    void beginIssueBatch(List<Coupon> coupons);

    void couponOverBatch(List<Coupon> coupons);
}
