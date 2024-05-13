package com.xc.media.domain.query;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileMediaQuery extends PageQuery {

    /**
     * 名称
     */
    private String name;
}
