package com.xc.trade.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

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
     * 优惠券id list
     */
    private String  couponId;

    /**
     * 收货地址
     */
    private String  address;

    private Integer deleted;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long updater;

    private Long creater;


}
