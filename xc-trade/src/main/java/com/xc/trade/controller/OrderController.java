package com.xc.trade.controller;

import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.trade.entity.dto.OrderDTO;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.query.OrderQuery;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.service.IOrderService;
import com.xc.trade.entity.vo.OrderVO;
import feign.Param;
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
     * 根据购物车预览订单
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
     * 管理员根据订单id发货
     * @param id
     * @return
     */
    @PostMapping("/delivery/{id}")
    public boolean delivery(@PathVariable Long id){
        if(!orderService.delivery(id)){
            throw new BizIllegalException("未支付货id不存在，无法发货");
        }
        return true;
    }

    /**
     * 管理员删除订单
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id){
        if(!orderService.deleteOrder(id)){
            throw new BizIllegalException("订单关闭或成功才可删除");
        }
        return true;
    }

    /**
     * 个人确定收货
     * @param id
     * @return
     */
    @PostMapping("/finish/{id}")
    public boolean finishOrder(@PathVariable Long id){
        return orderService.finishOrder(id);
    }

    /**
     * 分页查询，可指定spuName
     * @param query
     * @return
     */
    @GetMapping("/page")
    public PageDTO<OrderDTO> pageQuery(@Param("page")OrderQuery query){
        return orderService.pageQuery(query);
    }

    /**
     * 取消订单(未支付时)
     * @param id 订单id
     * @return
     */
    @PostMapping("/cancel/{id}")
    public boolean cancelOrder(@PathVariable Long id){
        return orderService.canceledOrder(id);
    }

}
