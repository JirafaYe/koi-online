package com.xc.trade.entity.dto;

import com.xc.trade.entity.vo.AddressVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
public class OrderDTO {
    private Long id;

    /**
     * 订单有效or关闭or退款中or订单已完成（收货）
     */
    private Integer status;

    /**
     * 是否发货
     */
    private Integer deliveryStatus;

    /**
     * null则未支付
     */
    private Long paymentId;

    /**
     * 原始金额 *100
     */
    private Integer rawPrice;

    /**
     * 优惠后金额 *100
     */
    private Integer finalPrice;

    /**
     * 退款金额
     */
    private Integer refundPrice;

    /**
     * 订单详情list
     */
    private List<OrderDetailsDTO> details;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 收货地址
     */
    private AddressVO addressVO;

    private LocalDateTime createTime;
}
