package com.xc.api.client.product;

import com.xc.api.client.product.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "product-service", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {
}
