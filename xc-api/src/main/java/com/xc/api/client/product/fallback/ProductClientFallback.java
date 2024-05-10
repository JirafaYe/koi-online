package com.xc.api.client.product.fallback;

import com.xc.api.client.product.ProductClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class ProductClientFallback implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
        return null;
    }
}
