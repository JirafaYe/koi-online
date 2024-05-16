package com.xc.api.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class LongIdsVO {
    /**
     * id集合
     */
    private List<Long> ids;
}
