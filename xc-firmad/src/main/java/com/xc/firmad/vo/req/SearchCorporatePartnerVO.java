package com.xc.firmad.vo.req;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

@Data
public class SearchCorporatePartnerVO extends PageQuery {

    /**
     * 模糊搜索条件
     */
    private String name ;

}
