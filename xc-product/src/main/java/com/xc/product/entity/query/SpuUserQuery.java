package com.xc.product.entity.query;

import com.xc.common.domain.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpuUserQuery extends PageQuery {
    /**
     * 可为空
     */
    private String spuName;
}
