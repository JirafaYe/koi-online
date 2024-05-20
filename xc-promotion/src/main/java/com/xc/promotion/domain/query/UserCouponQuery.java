package com.xc.promotion.domain.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户优惠券查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCouponQuery extends PageQuery {

    /**
     * 优惠券状态，1：未使用，2：已使用，3：已过期
     */
    private Integer status;
}
