package com.xc.product.entity.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SkuNumVO {
    private Map<Long,Integer> numMap;
}
