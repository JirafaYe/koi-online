package com.xc.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class SkuAttributesVO implements Serializable {
    /**
     * spu id
     */
    private Long spuId;

    /**
     * 属性map
     */
    private Map<String , Set<String>> resultMap;

}
