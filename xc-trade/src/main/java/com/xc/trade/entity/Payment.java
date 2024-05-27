package com.xc.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment")
public class Payment implements Serializable {
    /**
     * 支付成功生成的订单号，作为支付的唯一凭证
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 支付总金额
     */
    private double totalAmount;

    /**
     * 支付成功后生成的商户订单号，退款时需要
     */
    private Long merchantId;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 存储支付信息
     */
    private String content;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long updater;

    private Long creater;

}
