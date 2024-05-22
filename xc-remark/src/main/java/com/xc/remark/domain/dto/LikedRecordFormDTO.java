package com.xc.remark.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 点赞记录实体
 */
@Data
public class LikedRecordFormDTO {

    /**
     * 点赞业务id
     */
    @NotNull(message = "业务id不能为空")
    private Long bizId;

    /**
     * 点赞业务类型review ， reply
     */
    @NotNull(message = "业务类型不能为空")
    private String bizType;

    /**
     * 是否点赞，true：点赞；false：取消点赞
     */
    @NotNull(message = "是否点赞不能为空")
    private Boolean liked;
}
