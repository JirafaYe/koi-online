package com.xc.trade.service;

import com.xc.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.dto.PreviewOrderDTO;
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
public interface IOrderService extends IService<Order> {

    PreviewOrderDTO preViewFromChart(List<Long> shoppingCharts);

}
