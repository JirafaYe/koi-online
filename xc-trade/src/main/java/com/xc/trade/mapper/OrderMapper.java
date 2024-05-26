package com.xc.trade.mapper;

import com.xc.trade.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.trade.entity.vo.FlowReportsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<FlowReportsVO> getflowReports();
}
