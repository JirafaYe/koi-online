package com.xc.trade.mapper;

import com.xc.trade.entity.po.OrderDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {
    @Select("select distinct order_id from order_details where spu_name like %#{spuName}%")
    List<Long> selectOrdersBySpuName(String spuName);
}
