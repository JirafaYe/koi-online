package com.xc.common.interceptors;


import com.xc.common.constants.JwtConstant;
import com.xc.common.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRelayUserInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            return;
        }
        template.header(JwtConstant.USER_HEADER, userId.toString());
    }
}
