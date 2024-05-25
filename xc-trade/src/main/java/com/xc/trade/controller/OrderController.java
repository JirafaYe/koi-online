package com.xc.trade.controller;

import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.service.IOrderService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    IOrderService orderService;

    /**
     * 根据购物车预览订单金额
     * @param ids
     * @return
     */
    @GetMapping("/pre")
    public PreviewOrderDTO preViewFromChart(@RequestParam List<Long> ids){
        return orderService.preViewFromChart(ids);
    }
}
