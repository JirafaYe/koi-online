package com.xc.product.controller;

import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.UserContext;
import com.xc.product.entity.dto.BrandDTO;
import com.xc.product.entity.query.BrandQuery;
import com.xc.product.entity.vo.BrandPageVO;
import com.xc.product.entity.vo.BrandVO;
import com.xc.product.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Boolean createBrand(@RequestBody @Valid BrandVO vo){
//        UserContext.setUser(1790297174534524928L);
        return brandService.createBand(vo);
    }

    @PostMapping("/remove/{id}")
    public boolean removeBrand(@PathVariable Long id){
//        UserContext.setUser(1790297174534524928L);
        return brandService.removeBrand(id);
    }

    @GetMapping("/page")
    public PageDTO<BrandPageVO> queryByPage(BrandQuery query){
//        UserContext.setUser(1790297174534524928L);
        return brandService.queryBrandsByPage(query);
    }

    @PostMapping("/update")
    public boolean updateBrand(@RequestBody BrandVO vo){
        return brandService.updateBrand(vo);
    }
}
