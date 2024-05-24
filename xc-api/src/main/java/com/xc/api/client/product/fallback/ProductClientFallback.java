package com.xc.api.client.product.fallback;

import com.xc.api.client.product.ProductClient;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.product.SpuPageVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class ProductClientFallback implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient() {

            @Override
            public String queryCategoryById(Long id) {
                return "";
            }

            @Override
            public List<SpuPageVO> queryByName(String name) {
                return List.of();
            }

            @Override
            public List<SpuPageVO> queryById(Iterable<Long> ids) {
                return List.of();
            }

            @Override
            public List<String> queryCategoryByIdList(Iterable<Long> ids) {
                return List.of();
            }

            @Override
            public List<SkuPageVO> getSkuById(Iterable<Long> skuID) {
                return List.of();
            }
        };
    }
}
