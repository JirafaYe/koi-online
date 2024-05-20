package com.xc.product.controller;

import com.xc.product.entity.vo.SkuVO;
import com.xc.product.service.IStockKeepingUnitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 商品sku
 */
@RestController
@RequestMapping("/sku")
public class SkuController {
    @Resource
    IStockKeepingUnitService skuService;

    /**
     * 创建SKU
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean create(@Valid @RequestBody SkuVO vo){
        return skuService.createSku(vo);
    }

    /**
     * 根据id删除sku
     * @param id skuId
     * @return
     */
    @PostMapping("/remove/{id}")
    public boolean removeById(@PathVariable("id") Long id){
        return skuService.removeSku(id);
    }
}
