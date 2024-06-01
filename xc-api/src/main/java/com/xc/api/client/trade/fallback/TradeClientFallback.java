package com.xc.api.client.trade.fallback;

import com.xc.api.client.trade.TradeClient;
import com.xc.common.utils.CollUtils;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class TradeClientFallback implements FallbackFactory<TradeClient> {
    @Override
    public TradeClient create(Throwable cause) {
        return new TradeClient() {
            @Override
            public List<Long> getSKuIds(Iterable<Long> detailsIds) {
                return CollUtils.emptyList();
            }
        };
    }
}
