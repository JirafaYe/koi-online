package com.xc.trade.service;

import com.xc.trade.entity.po.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.OrderVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface IOrderService extends IService<Orders> {

    PreviewOrderDTO preViewFromChart(List<Long> shoppingCharts);

    boolean createOrder(OrderVO vo);

    List<FlowReportsVO> flowReports();

    List<GoodsSpuReportsVO> achieveReports();

    List<GoodsCategroyReportsVO> marketingReports();

    /**
     * 管理员发货
     * @param orderId
     * @return
     */
    boolean delivery(Long orderId);

    /**
     * 收货
     * @param orderId
     * @return
     */
    boolean finishOrder(Long orderId);

    boolean deleteOrder(Long orderId);

}
