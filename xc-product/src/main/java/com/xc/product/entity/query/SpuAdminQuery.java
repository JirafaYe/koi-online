package com.xc.product.entity.query;

import com.xc.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SpuAdminQuery extends SpuUserQuery{
    public SpuAdminQuery(SpuUserQuery userQuery) {
        super(userQuery.getSpuName());
    }

    /**
     * 是否上架
     */
    private Boolean available;

    /**
     * 申请开始时间
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    /**
     * 申请结束时间
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime endTime;
}
