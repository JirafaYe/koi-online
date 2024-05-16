package com.xc.promotion.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 兑换码实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCodeVo {

    /**
     * 兑换码id
     */
    private Integer id;

    /**
     * 兑换码
     */
    private String code;
}
