package com.xc.promotion.domain.vo;

import com.xc.promotion.domain.enums.CouponStatus;
import com.xc.promotion.domain.enums.DiscountType;
import com.xc.promotion.domain.enums.ObtainType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券分页数据
 */
@Data
public class CouponPageVO {

    /**
     * 优惠券id
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 是否限定使用范围
     */
    private Boolean specific;

    /**
     * 优惠券类型，1：每满减，2：折扣，3：无门槛，4：普通满减
     */
    private DiscountType discountType;

    /**
     * 折扣门槛，0代表无门槛
     */
    private Integer thresholdAmount;

    /**
     * 折扣值，满减填抵扣金额；打折填折扣值：80标示打8折
     */
    private Integer discountValue;

    /**
     * 最大优惠金额
     */
    private Integer maxDiscountAmount;

    /**
     * 获取方式1：手动领取，2：指定发放（通过兑换码兑换）
     */
    private ObtainType obtainWay;

    /**
     * 已使用数量
     */
    private Integer usedNum;

    /**
     * 已发放数量
     */
    private Integer issueNum;

    /**
     * 优惠券总量
     */
    private Integer totalNum;

    /**
     * 优惠券创建时间
     */
    private LocalDateTime createTime;

    /**
     * 发放开始时间
     */
    private LocalDateTime issueBeginTime;

    /**
     * 发放结束时间
     */
    private LocalDateTime issueEndTime;

    /**
     * 有效天数
     */
    private Integer termDays;

    /**
     * 使用有效期开始时间
     */
    private LocalDateTime termBeginTime;

    /**
     * 使用有效期结束时间
     */
    private LocalDateTime termEndTime;

    /**
     * 状态
     */
    private CouponStatus status;
}
