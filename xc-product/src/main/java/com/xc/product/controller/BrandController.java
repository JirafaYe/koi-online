package com.xc.product.controller;

import com.xc.common.utils.UserContext;
import com.xc.product.entity.vo.BrandVO;
import com.xc.product.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 品牌管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final IBrandService brandService;

    /**
     * 创建品牌
     * @param vo
     * @return 是否成功创建
     */
    @PostMapping("/create")
    public Boolean createBrand(@Valid BrandVO vo){
        UserContext.setUser(1790297174534524928L);
        return brandService.createBand(vo);
    }
}
