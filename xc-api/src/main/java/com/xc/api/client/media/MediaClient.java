package com.xc.api.client.media;

import com.xc.api.client.media.fallback.MediaClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "media-service", fallbackFactory = MediaClientFallback.class)
public interface MediaClient {
}
