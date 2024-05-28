package com.xc.trade.entity.dto;

import lombok.Data;

/**
 * 退款取消
 */
@Data
public class RefundCancelDTO {

    /**
     * 退款申请id，订单明细id和退款申请id二选一
     */
    private Long id;

    /**
     * 订单明细id，订单明细id和退款申请id二选一
     */
    private Long orderDetailId;
}
