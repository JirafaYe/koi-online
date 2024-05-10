package com.xc.api.client.user;

import com.xc.api.client.user.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service", fallbackFactory = UserClientFallback.class)
public interface UserClient {
}
