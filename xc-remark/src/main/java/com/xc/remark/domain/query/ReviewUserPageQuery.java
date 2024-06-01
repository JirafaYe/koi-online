package com.xc.remark.domain.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

@Data
public class ReviewUserPageQuery extends PageQuery {
    private Long spuId;
}
