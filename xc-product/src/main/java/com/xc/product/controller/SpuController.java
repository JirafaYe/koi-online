package com.xc.product.controller;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;
import com.xc.product.service.IStandardProductUnitService;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 创建spu
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean createSpu(@RequestBody @Valid SpuVO vo){
        return spuService.createSpu(vo);
    }

    /**
     * 更新spu
     * @param vo
     * @return
     */
    @PostMapping("/update")
    public boolean updateSpu(@RequestBody @Valid SpuVO vo){
        return spuService.updateSpu(vo);
    }

    /**
     * 删除spu，同时删除所有sku
     * @param id spuId
     * @return
     */
    @PostMapping("/{id}")
    public boolean remove(@PathVariable Long id){
        return spuService.removeSpu(id);
    }

    /**
     * 分页查询，可指定brand和category
     * @param query
     * @return
     */
    @GetMapping("/query")
    public PageDTO<SpuPageVO> queryPage(SpuQuery query){
        return spuService.queryByPage(query);
    }
}
