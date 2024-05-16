package com.xc.user.vo.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class LongIdsVO {
    /**
     * id集合
     */
    @NotEmpty(message = "id不能为空")
    private List<Long> ids;
}
