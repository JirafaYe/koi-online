package com.xc.api.client.promotion;

import com.xc.api.client.promotion.fallback.PromotionClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "promotion-service", fallbackFactory = PromotionClientFallback.class)
public interface PromotionClient {

    @GetMapping("/coupon-scope/{id}")
    Boolean judgeCouponExist(@PathVariable("id") Long id);
}
