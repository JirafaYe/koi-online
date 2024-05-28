package com.xc.trade.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.dto.ShoppingChartDTO;
import com.xc.trade.entity.query.ShoppingChartQuery;
import com.xc.trade.entity.vo.ShoppingChartVO;
import com.xc.trade.service.IShoppingChartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/shopping")
public class ShoppingChartController {
    @Resource
    IShoppingChartService shoppingChartService;

    /**
     * 添加购物车
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean create(@Valid @RequestBody ShoppingChartVO vo) {
        return shoppingChartService.create(vo);
    }

    /**
     * 更新单条购物车
     * @param vo
     * @return
     */
    @PostMapping("/update")
    public boolean update(@Valid@RequestBody ShoppingChartVO vo){
        return shoppingChartService.update(vo);
    }

    /**
     * 批量删除
     * @param ids id列表
     * @return
     */
    @PostMapping("/remove")
    boolean remove(@RequestBody List<Long> ids){
        return shoppingChartService.remove(ids);
    }

    /**
     * 购物车分页查询
     * @param query
     * @return
     */
    @GetMapping("/page")
    PageDTO<ShoppingChartDTO> pageQuery(ShoppingChartQuery query){
        return shoppingChartService.pageQuery(query);
    }
}
