package com.xc.api.client.firmad.fallback;

import com.xc.api.client.firmad.FirmadClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class FirmadClientFallback implements FallbackFactory<FirmadClient> {
    @Override
    public FirmadClient create(Throwable cause) {
        return new FirmadClient() {
            @Override
            public String test() {
                return "1111";
            }
        };
    }
}
