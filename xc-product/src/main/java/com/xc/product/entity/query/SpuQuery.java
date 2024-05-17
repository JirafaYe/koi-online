package com.xc.product.entity.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

/**
 * 必须且仅支持传一个值，传两值则默认根据brandId查询
 */
@Data
public class SpuQuery extends PageQuery {
    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long categoryId;
}
