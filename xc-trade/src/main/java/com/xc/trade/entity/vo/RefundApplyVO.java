package com.xc.trade.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 退款申请详细信息
 */
@Data
public class RefundApplyVO {

    /**
     * 退款id
     */
    private Long id;

    /**
     * 子订单id
     */
    private Long orderDetailId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付流水单号
     */
    private Long payOrderNo;

    /**
     * 退款流水单号
     */
    private Long refundOrderNo;

    /**
     * 申请退款原因
     */
    private String refundReason;

    /**
     * 申请退款说明
     */
    private String questionDesc;

    /**
     * 账户
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 订单时间
     */
    private LocalDateTime orderTime;

    /**
     * 支付时间
     */
    private LocalDateTime paySuccessTime;

    /**
     * 退款申请时间
     */
    private LocalDateTime createTime;

    /**
     * 退款审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 状态描述
     */
    private String message;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 审批备注
     */
    private String remark;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 实付金额
     */
    private Integer realPayAmount;

    /**
     * 优惠券规则
     */
    private String couponDesc;

    /**
     * 优惠总金额
     */
    private Integer discountAmount;


    /**
     * 退款状态，1：待审批，2：取消退款，3：同意退款/同意退货，4：待买家寄出，5：待商家收货， 6：拒绝退款，7：退款成功，8：退款失败
     */
    private Integer status;

    /**
     * 退款失败原因
     */
    private String failedReason;
}
