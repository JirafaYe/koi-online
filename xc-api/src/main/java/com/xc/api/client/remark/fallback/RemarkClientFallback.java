package com.xc.api.client.remark.fallback;

import com.xc.api.client.remark.RemarkClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class RemarkClientFallback implements FallbackFactory<RemarkClient> {
    @Override
    public RemarkClient create(Throwable cause) {
        return null;
    }
}
