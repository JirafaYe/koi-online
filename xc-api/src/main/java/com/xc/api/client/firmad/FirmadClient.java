package com.xc.api.client.firmad;

import com.xc.api.client.firmad.fallback.FirmadClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "firmad-service", fallbackFactory = FirmadClientFallback.class)
public interface FirmadClient {
    @GetMapping("/test")
    public String test();
}
