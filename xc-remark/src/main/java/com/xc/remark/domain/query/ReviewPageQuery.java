package com.xc.remark.domain.query;

import com.xc.common.domain.query.PageQuery;
import com.xc.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 评论分页查询条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewPageQuery extends PageQuery {

    private String productName;

    /**
     *  "更新时间区间的开始时间", example = "2022-7-18 19:52:36"
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime beginTime;

    /**
     * 更新时间区间的结束时间", example = "2022-7-18 19:52:36"
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime endTime;
}
