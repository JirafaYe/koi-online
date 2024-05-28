package com.xc.trade.entity.dto;

import com.xc.trade.entity.enums.RefundClassifyStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 退款申请
 */
@Data
public class RefundFormDTO {

    /**
     * 订单明细id
     */
    @NotNull(message = "请选中退款订单")
    private Long orderDetailId;

    /**
     * 退款类别，1：仅退款，2：退货退款
     */
    @NotNull(message = "请选择退款类别")
    private RefundClassifyStatus refundClassify;

    /**
     * 退款原因
     */
    @NotNull(message = "请选择退款原因")
    private String refundReason;

    /**
     * 问题说明
     */
    @NotNull(message = "问题说明不能为空")
    private String questionDesc;
}