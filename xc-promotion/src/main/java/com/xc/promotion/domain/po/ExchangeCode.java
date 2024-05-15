package com.xc.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xc.promotion.domain.enums.ExchangeCodeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 兑换码
 *
 * @author Koi
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("exchange_code")
public class ExchangeCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 兑换码id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 兑换码
     */
    private String code;

    /**
     * 兑换码状态， 1：待兑换，2：已兑换，3：兑换活动已结束
     */
    private ExchangeCodeStatus status;

    /**
     * 兑换人
     */
    private Long userId;

    /**
     * 兑换码目标id，该id则是优惠券的id
     */
    private Long exchangeTargetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 兑换码过期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
