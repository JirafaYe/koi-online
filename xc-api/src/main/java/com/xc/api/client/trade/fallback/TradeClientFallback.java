package com.xc.api.client.trade.fallback;

import com.xc.api.client.trade.TradeClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class TradeClientFallback implements FallbackFactory<TradeClient> {
    @Override
    public TradeClient create(Throwable cause) {
        return null;
    }
}
