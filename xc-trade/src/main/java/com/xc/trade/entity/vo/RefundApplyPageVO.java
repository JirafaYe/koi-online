package com.xc.trade.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 退款信息
 */
@Data
public class RefundApplyPageVO {

    /**
     * 退款id
     */
    private Long id;

    /**
     * 订单明细id
     */
    private Long orderDetailId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 申请人
     */
    private String proposerName;

    /**
     * 申请人手机号
     */
    private String proposerMobile;

    /**
     * 退款申请状态
     */
    private Integer status;

    /**
     * 退款申请状态描述
     */
    private String refundStatusDesc;

    /**
     * 退款申请时间
     */
    private LocalDateTime createTime;

    /**
     * 审批人
     */
    private String approverName;

    /**
     * 审批时间
     */
    private String approveTime;

    /**
     * 退款成功时间
     */
    private LocalDateTime refundSuccessTime;
}
