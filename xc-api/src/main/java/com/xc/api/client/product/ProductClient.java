package com.xc.api.client.product;

import com.xc.api.client.product.fallback.ProductClientFallback;
import com.xc.api.dto.product.SpuPageVO;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "product-service", fallbackFactory = ProductClientFallback.class)
public interface ProductClient {
    /**
     * 根据id查询category
     * @param id 目录id
     * @return
     */
    @GetMapping("/category/{id}")
    String queryCategoryById(@PathVariable Long id);

    /**
     * 内部暴露的api接口 根据名字模糊查询
     * @param name spu
     * @return
     */
    @GetMapping("/spu/name")
    List<SpuPageVO> queryByName(@Param("name") String name);
}
