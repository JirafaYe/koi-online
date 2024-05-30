package com.xc.product.controller;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xc.api.dto.IdAndNumDTO;
import com.xc.api.dto.product.SkuNumVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.JsonUtils;
import com.xc.product.entity.query.SkuQuery;
import com.xc.product.entity.vo.SkuAttributesVO;
import com.xc.product.entity.vo.SkuListVO;
import com.xc.product.entity.vo.SkuPageVO;
import com.xc.product.entity.vo.SkuVO;
import com.xc.product.service.IStockKeepingUnitService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public boolean create(@Valid @RequestBody SkuListVO vo){
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

    /**
     * 更新sku
     * @param vo
     * @return
     */
    @PostMapping("/update")
    public boolean update(@Valid @RequestBody SkuVO vo){
        return skuService.updateSku(vo);
    }

    /**
     * 分页查询Sku(管理员)
     * @param query
     * @return
     */
    @GetMapping("/query")
    public PageDTO<SkuPageVO> queryByPage(@Valid SkuQuery query){
        return skuService.queryPageBySpuId(query);
    }

    /**
     * 根据spuId获取sku配置map
     * @param id spuId
     * @return
     */
    @GetMapping("/{id}")
    public SkuAttributesVO getAttributes(@PathVariable Long id){
        return new SkuAttributesVO(id,skuService.getAttributes(id));
    }

    /**
     * 根据配置获取详细sku信息
     * @param attributes 配置，json格式的字符串
     * @param spuId spuId
     * @return
     */
    @GetMapping("/detail")
    public SkuPageVO getSkuByAttributes(@Param("attributes") String attributes,@Param("spu") Long spuId){
        return skuService.getSkuByAttributes(attributes,spuId);
    }

    /**
     * 内部api，根据id获取sku
     * @param ids
     * @return
     */
    @GetMapping("/details")
    public List<SkuPageVO> getSkuById(@RequestBody List<Long> ids){
        return skuService.getSkuById(ids);
    }

    /**
     * 内部api，修改sku数量
     * @return
     */
    @PostMapping("/num")
    public boolean updateSkuNum(@RequestBody List<IdAndNumDTO> list) {
        Map<Long, Integer> map = IdAndNumDTO.toMap(list);
        skuService.updateSkuNum(map);
        return true;
    }

    /**
     * 修改sku 上架or下架
     * @param id
     * @return
     */
    @PostMapping("/available/{id}")
    public boolean changeAvailable(@PathVariable Long id){
        return skuService.changeAvailable(id);
    }
}
