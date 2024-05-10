package com.xc.api.client.trade;

import com.xc.api.client.trade.fallback.TradeClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "trade-service", fallbackFactory = TradeClientFallback.class)
public interface TradeClient {
}
