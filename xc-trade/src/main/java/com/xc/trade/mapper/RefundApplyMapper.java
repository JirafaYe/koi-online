package com.xc.trade.mapper;

import com.xc.trade.entity.po.RefundApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 退款申请 Mapper 接口
 * </p>
 *
 * @author Koi
 * @since 2024-05-28
 */
public interface RefundApplyMapper extends BaseMapper<RefundApply> {

    List<RefundApply> queryByDetailId(@Param("detailId") Long detailId);
}
