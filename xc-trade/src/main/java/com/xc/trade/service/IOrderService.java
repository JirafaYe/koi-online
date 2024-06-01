package com.xc.trade.service;

import com.xc.api.dto.product.SkuPageVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.dto.OrderDTO;
import com.xc.trade.entity.dto.ReportsByRangeReqDTO;
import com.xc.trade.entity.po.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.query.OrderQuery;
import com.xc.trade.entity.vo.*;

import javax.servlet.http.HttpServletResponse;
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

    void validateSkuPageVO(List<SkuPageVO> skuVOs);

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

    boolean canceledOrder(Long orderId);

    OrderDTO queryById(Long orderId);

    PageDTO<OrderDTO> pageQuery(OrderQuery query,boolean isAdmin);

    List<Long> getSKuIds(List<Long> detailsIds);

    ReportsByRangeVO getReportsByRange(ReportsByRangeReqDTO reportsByRangeVO);

    void downLoadFlowReports(HttpServletResponse res);
}
