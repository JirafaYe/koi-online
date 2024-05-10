package com.xc.api.client.log;

import com.xc.api.client.log.fallback.LogClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "log-service", fallbackFactory = LogClientFallback.class)
public interface LogClient {
}
