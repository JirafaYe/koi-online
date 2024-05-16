package com.xc.api.client.product;

import com.xc.api.client.product.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product-service", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {
    /**
     * 根据id查询category
     * @param id 目录id
     * @return
     */
    @GetMapping("/category/{id}")
    String queryCategoryById(@PathVariable Long id);
}
