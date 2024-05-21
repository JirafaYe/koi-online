package com.xc.product.entity.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class SkuQuery extends PageQuery {
    /**
     * spuId
     */
    @NotNull
    private Long spuId;

    /**
     * 是否上架
     */
    @NotNull
    private boolean available;
}
