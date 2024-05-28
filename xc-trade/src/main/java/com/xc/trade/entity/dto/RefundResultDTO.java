package com.xc.trade.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付结果
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RefundResultDTO {

    public static final int RUNNING = 1;
    public static final int FAILED = 2;
    public static final int SUCCESS = 3;
    public static final String OK = "ok";

    /**
     * 退款状态，1：退款中，2：退款失败，3：退款成功
     */
    private int status;

    /**
     * 支付失败原因
     */
    private String msg;

    /**
     * 业务端支付订单号
     */
    private Long PayOrderId;

    /**
     * 业务端退款订单号
     */
    private Long RefundOrderId;

    /**
     * 支付流水交易单号
     */
    private Long payOrderNo;

    /**
     * 退款交易单号
     */
    private Long refundOrderNo;

    public static RefundResultDTOBuilder success() {
        return builder().status(SUCCESS).msg(OK);
    }

    public static RefundResultDTOBuilder running() {
        return builder().status(RUNNING);
    }

    public static RefundResultDTOBuilder failed() {
        return builder().status(FAILED);
    }
}
