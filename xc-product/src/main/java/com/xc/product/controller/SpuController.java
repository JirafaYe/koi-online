package com.xc.product.controller;

import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.UserContext;
import com.xc.product.entity.dto.SpuPageDTO;
import com.xc.product.entity.query.SpuAdminQuery;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.query.SpuUserQuery;
import com.xc.product.entity.vo.SpuDetailsAdminVO;
import com.xc.product.entity.vo.SpuDetailsVO;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;
import com.xc.product.service.IStandardProductUnitService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
    public SpuPageDTO<SpuPageVO> queryPage(SpuQuery query){
        return spuService.queryByPage(query);
    }

    /**
     * 内部暴露的api接口 根据名字模糊查询
     * @param name spu
     * @return
     */
    @GetMapping("/name")
    public List<SpuPageVO> queryByName(@Param("name") String name){
        return spuService.queryByName(name);
    }

    /**
     * 内部暴露的api接口 根据id查询，返回id和spuName
     * @param ids spu id list
     * @return
     */
    @GetMapping("/ids")
    public List<SpuPageVO> queryById(@RequestBody List<Long> ids){
        return spuService.queryById(ids);
    }

    /**
     * 修改spu 上架or下架
     * @param id
     * @return
     */
    @PostMapping("/available/{id}")
    public boolean changeAvailable(@PathVariable Long id){
        return spuService.changeAvailable(id);
    }

    /**
     * 用户查询，可不登录
     * @param query
     * @return
     */
    @GetMapping("/page/user")
    public SpuPageDTO<SpuPageVO> userQuery(SpuUserQuery query){
        if(UserContext.getUser()==null){
            query.setSpuName(null);
        }
        return spuService.pageQuery(new SpuAdminQuery(query),false);
    }

    /**
     * 管理员查询
     * @param query
     * @return
     */
    @GetMapping("/page/admin")
    public SpuPageDTO<SpuPageVO> adminQuery(SpuAdminQuery query){
        return spuService.pageQuery(query,true);
    }

    /**
     * 根据spuId获取详情
     * @param spuId
     * @return
     */
    @GetMapping("/details")
    public SpuDetailsVO queryById(@Param("id") Long spuId){
        return spuService.queryById(spuId);
    }

    /**
     * 管理员根据spuId获取详情
     * @param spuId
     * @return
     */
    @GetMapping("/details/admin")
    public SpuDetailsAdminVO queryByIdAdmin(@Param("id") Long spuId){
        return spuService.queryByIdAdmin(spuId);
    }
}
