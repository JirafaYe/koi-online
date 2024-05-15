package com.xc.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 优惠券作用范围信息
 *
 * @author Koi
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupon_scope")
public class CouponScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;



    /**
     * 范围限定类型：1-分类，2-商品，等等
     */
    private Integer type;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券作用范围的业务id，例如分类id、商品id
     */
    private Long bizId;
}
