package com.xc.product.controller;

import com.xc.product.entity.vo.SkuVO;
import com.xc.product.service.IStockKeepingUnitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
