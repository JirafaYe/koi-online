package com.xc.product.controller;

import com.xc.product.entity.vo.SpuVO;
import com.xc.product.service.IStandardProductUnitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * spu(标准商品)
 */
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Resource
    IStandardProductUnitService spuService;

    @PostMapping("/create")
    public boolean createSpu(@Valid SpuVO vo){
        return spuService.createSpu(vo);
    }
}
