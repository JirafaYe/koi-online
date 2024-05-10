package com.xc.api.client.promotion;

import com.xc.api.client.promotion.fallback.PromotionClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "promotion-service", fallbackFactory = PromotionClientFallback.class)
public interface PromotionClient {
}
