package com.xc.trade.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundApplyDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 子订单id
     */
    private Long orderDetailId;

    /**
     * 退款金额
     */
    private Integer refundAmount;
}
