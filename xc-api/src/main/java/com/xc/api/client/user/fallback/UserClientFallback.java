package com.xc.api.client.user.fallback;

import com.xc.api.client.user.UserClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return null;
    }
}
