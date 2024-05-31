package com.xc.firmad.vo.req;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

@Data
public class FirmPageQuery extends PageQuery {
    private String name;
}
