package com.xc.api.client.product.fallback;

import com.xc.api.client.product.ProductClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class ProductClientFallback implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
        return id -> null;
    }
}
