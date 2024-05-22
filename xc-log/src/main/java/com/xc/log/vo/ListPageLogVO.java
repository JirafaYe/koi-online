package com.xc.log.vo;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListPageLogVO extends PageQuery {

    /**
     * 操作用户
     */
    private String user;
    /**
     * 操作模块
     */
    private String title;
    /**
     * 操作类型
     */
    private String opType;
    /**
     * 操作开始时间
     */
    private LocalDateTime opStartTime;
    /**
     * 操作结束时间
     */
    private LocalDateTime opEndTime;
    /**
     * 操作状态
     */
    private Integer opStatus;
}
