package com.xc.trade.entity.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQuery extends PageQuery {
    private String spuName;
}
