package com.xc.api.client.promotion.fallback;

import com.xc.api.client.promotion.PromotionClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class PromotionClientFallback implements FallbackFactory<PromotionClient> {
    @Override
    public PromotionClient create(Throwable cause) {
        return new PromotionClient() {
            @Override
            public Boolean judgeCouponExist(Long id) {
                return true;
            }
        };
    }
}
