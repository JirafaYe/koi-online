package com.xc.api.dto.promotion;

import lombok.Data;
import lombok.experimental.Accessors;

/***
 * 订单中的商品信息
 */
@Data
@Accessors(chain = true)
public class OrderProductDTO {

    /***
     * 商品id
     */
    private Long id;

    /**
     * 商品的三级分类id
     */
    private Long cateId;

    /**
     * 商品价格
     */
    private Integer price;
}
