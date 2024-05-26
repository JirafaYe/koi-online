package com.xc.trade.controller;

import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.service.IOrderService;
import com.xc.trade.entity.vo.OrderVO;
import com.xc.trade.entity.vo.FlowReportsVO;
import org.springframework.web.bind.annotation.*;

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
    /**
     * 创建订单
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean create(OrderVO vo){
        return orderService.createOrder(vo);
    }

    /**
     * 流量报表
     * @return
     */
    @GetMapping("/flowReports")
    public List<FlowReportsVO> flowReports(){
        return orderService.flowReports();
    }

}
