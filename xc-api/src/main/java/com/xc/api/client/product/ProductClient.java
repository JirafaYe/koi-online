package com.xc.api.client.product;

import com.xc.api.client.product.fallback.ProductClientFallback;
import com.xc.api.dto.IdAndNumDTO;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.product.SpuPageVO;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    /**
     * 内部暴露的api接口 根据id查询，返回id和spuName
     * @param ids spu id list
     * @return
     */
    @GetMapping("/spu/ids")
    List<SpuPageVO> queryById(@RequestBody Iterable<Long> ids);

    /**
     * 根据id list查询
     * @param ids 目录idList
     * @return
     */
    @GetMapping("/category/ids")
    List<String> queryCategoryByIdList(@RequestBody Iterable<Long> ids);

    @GetMapping("/sku/details")
    List<SkuPageVO> getSkuById(Iterable<Long> skuID);

    @PostMapping("/sku/num")
    boolean updateSkuNum(List<IdAndNumDTO> list);
}
