package com.xc.promotion.domain.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 兑换码查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CodeQuery extends PageQuery {

    /**
     * 兑换码对应的优惠券id
     */
    @NotNull
    private Long couponId;

    /**
     * 兑换码状态，1：未兑换，2：已兑换
     */
    @NotNull
    private Integer status;
}
