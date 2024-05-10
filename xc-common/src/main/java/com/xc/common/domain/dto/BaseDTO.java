package com.xc.common.domain.dto;


import java.time.LocalDateTime;

/**
 * DTO基础属性
 */
public class BaseDTO {

    /**
     * 创建人id
     */
    private Long creater;
    /**
     * 更新人id
     */
    private Long updater;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
