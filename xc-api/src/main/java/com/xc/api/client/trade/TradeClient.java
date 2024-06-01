package com.xc.api.client.trade;

import com.xc.api.client.trade.fallback.TradeClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "trade-service", fallbackFactory = TradeClientFallback.class)
public interface TradeClient {
    @GetMapping("/order/skues")
    List<Long> getSKuIds(@RequestBody Iterable<Long> detailsIds);
}
