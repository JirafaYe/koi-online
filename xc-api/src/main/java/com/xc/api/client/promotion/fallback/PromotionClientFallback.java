package com.xc.api.client.promotion.fallback;

import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.utils.CollUtils;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collections;
import java.util.List;

public class PromotionClientFallback implements FallbackFactory<PromotionClient> {
    @Override
    public PromotionClient create(Throwable cause) {
        return new PromotionClient() {
            @Override
            public Boolean judgeCouponExist(Long id) {
                return true;
            }

            @Override
            public List<CouponDiscountDTO> findDiscountSolution(List<OrderProductDTO> orderProducts) {
                return CollUtils.emptyList();
            }

            @Override
            public CouponDiscountDTO queryDiscountDetailByOrder(OrderCouponDTO orderCouponDTO) {
                return null;
            }

            @Override
            public void writeOffCoupon(List<Long> userCouponIds) {
                throw new BizIllegalException("使用优惠券失败");
            }

            @Override
            public void refundCoupon(List<Long> userCouponIds) {
                throw new BizIllegalException("退还优惠券失败");
            }

            @Override
            public List<String> queryDiscountRules(List<Long> userCouponIds) {
                return CollUtils.emptyList();
            }
        };
    }
}
