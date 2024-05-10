package com.xc.api.client.log.fallback;

import com.xc.api.client.log.LogClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class LogClientFallback implements FallbackFactory<LogClient> {
    @Override
    public LogClient create(Throwable cause) {
        return null;
    }
}
