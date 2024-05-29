package com.xc.trade.entity.query;

import com.xc.common.domain.query.PageQuery;
import com.xc.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 退款申请分页参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RefundApplyPageQuery extends PageQuery {

    /**
     * 退款id
     */
    private Long id;

    /**
     * 退款状态，1：待审批，2：取消退款，3：同意退款/同意退货，4：待买家寄出，5：待商家收货， 6：拒绝退款，7：退款成功，8：退款失败
     */
    private Integer refundStatus;

    /**
     * 1为仅退款，2为退货退款
     */
    private Integer refundClassifyStatus;

    /**
     * 订单明细id
     */
    private Long orderDetailId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 申请开始时间
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime applyStartTime;

    /**
     * 申请结束时间
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime applyEndTime;
}
