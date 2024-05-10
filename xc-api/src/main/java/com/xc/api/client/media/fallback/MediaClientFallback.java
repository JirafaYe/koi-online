package com.xc.api.client.media.fallback;

import com.xc.api.client.media.MediaClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class MediaClientFallback implements FallbackFactory<MediaClient> {
    @Override
    public MediaClient create(Throwable cause) {
        return null;
    }
}
