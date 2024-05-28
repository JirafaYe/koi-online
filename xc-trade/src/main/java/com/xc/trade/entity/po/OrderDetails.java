package com.xc.trade.entity.po;

import java.math.BigDecimal;
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
@TableName("order_details")
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    private String spuName;

    /**
     * spuId
     */
    private Long spuId;

    /**
     * 详细sku id
     */
    private Long skuId;

    /**
     * sku属性
     */
    private String attributes;

    /**
     * sku图片url
     */
    private String image;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 金额*100
     */
    private Integer price;

    /**
     * 不为空则退款，退款申请中0，退款申请成功1，已退款2
     */
    private Integer refundStatus;

    /**
     * 是否取消，默认false
     */
    private boolean canceled;

    private Integer deleted;


    /**
     * 最终金额
     */
    private Integer finalPrice;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long updater;

    private Long creater;


}
