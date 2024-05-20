package com.xc.promotion.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.utils.CollUtils;
import com.xc.promotion.domain.enums.CouponStatus;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.service.ICouponService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponIssueTaskHandler {

    private final ICouponService couponService;

    @XxlJob("couponIssueJobHandler")
    public void handleCouponIssueJob(){
        int index = XxlJobHelper.getShardIndex() + 1;
        int size = Integer.parseInt(XxlJobHelper.getJobParam());
        // 2.查询<<未开始>>的优惠券
        Page<Coupon> page = couponService.lambdaQuery()
                .eq(Coupon::getStatus, CouponStatus.UN_ISSUE)
                .le(Coupon::getIssueBeginTime, LocalDateTime.now())
                .page(new Page<>(index, size));
        // 3.发放优惠券
        List<Coupon> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return;
        }
        couponService.beginIssueBatch(records);
    }

    @XxlJob("couponOverJobHandler")
    public void handleCouponOverJob(){
        int index = XxlJobHelper.getShardIndex() + 1;
        int size = Integer.parseInt(XxlJobHelper.getJobParam());
        // 2.查询<<进行中>>的优惠券
        Page<Coupon> page = couponService.lambdaQuery()
                .eq(Coupon::getStatus, CouponStatus.ISSUING)
                .le(Coupon::getIssueEndTime, LocalDateTime.now())
                .page(new Page<>(index, size));
        // 3.发放优惠券
        List<Coupon> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return;
        }
        couponService.couponOverBatch(records);
    }
}
