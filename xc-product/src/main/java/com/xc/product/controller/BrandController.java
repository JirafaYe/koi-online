package com.xc.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        return brandService.createBand(vo);
    }

    /**
     * 删除品牌
     * @param id
     */
    @PostMapping("/remove/{id}")
    public Boolean removeBrand(@PathVariable Long id){
        return brandService.removeBrand(id);
    }

    /**
     * 查询品牌
     * @param query
     * @return
     */
    @GetMapping("/page")
    public PageDTO<BrandPageVO> queryByPage(BrandQuery query){
        return brandService.queryBrandsByPage(query);
    }

    /**
     * 更新品牌
     * @param vo
     * @return
     */
    @PostMapping("/update")
    public Boolean updateBrand(@RequestBody BrandVO vo){
        return brandService.updateBrand(vo);
    }
}
