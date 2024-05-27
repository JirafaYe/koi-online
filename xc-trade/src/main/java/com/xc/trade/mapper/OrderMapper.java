package com.xc.trade.mapper;

import com.xc.trade.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface OrderMapper extends BaseMapper<Orders> {

    List<FlowReportsVO> getflowReports();
    List<GoodsSpuReportsVO> getAchieveReports();

    List<GoodsCategroyReportsVO> getMarketingReports();

}
