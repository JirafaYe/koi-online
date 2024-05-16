package com.xc.promotion.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券使用范围
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponScopeVO {

    private Long id;

    /**
     * 范围名称
     */
    private String name;
}
