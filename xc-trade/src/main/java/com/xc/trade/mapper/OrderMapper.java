package com.xc.trade.mapper;

import com.xc.trade.entity.po.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
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

    @Update("update orders set delivery_status=1 where id=#{orderId} and payment_id is not null")
    int deliveryOrder(Long orderId);

    @Update("update orders set status=#{status} where id=#{orderId}")
    int updateOrderStatusByAdmin(int status,Long orderId);

    @Update("update orders set status=#{status} where id=#{orderId} and user_id=#{userId} and payment_id is not null")
    int updateOrderStatusByUser(int status,Long orderId,Long userId);

}
