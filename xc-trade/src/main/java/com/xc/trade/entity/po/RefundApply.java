package com.xc.trade.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xc.trade.entity.enums.RefundClassifyStatus;
import com.xc.trade.entity.enums.RefundStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款申请
 * </p>
 *
 * @author Koi
 * @since 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_apply")
public class RefundApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 流水支付单号
     */
    private Long payOrderNo;

    /**
     * 流水退款单号
     */
    private Long refundOrderNo;

    /**
     * 订单所属用户id
     */
    private Long userId;

    /**
     * 退款类别，1：仅退款，2：退货退款
     */
    private RefundClassifyStatus refundClassify;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 退款状态，1：待审批，2：取消退款，3：同意退款/同意退货，4：待买家寄出，5：待商家收货， 6：拒绝退款，7：退款成功，8：退款失败
     */
    private RefundStatus status;

    /**
     * 申请退款原因
     */
    private String refundReason;

    /**
     * 退款状态描述
     */
    private String message;

    /**
     * 审批人id
     */
    private Long approver;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 审批备注
     */
    private String remark;

    /**
     * 退款失败原因
     */
    private String failedReason;

    /**
     * 退款问题说明
     */
    private String questionDesc;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 退款完成时间（成功或失败）
     */
    private LocalDateTime finishTime;

    /**
     * 创建退款申请时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;


}
