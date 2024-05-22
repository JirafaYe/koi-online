package com.xc.remark.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 回复实体类
 */
@Data
public class ReplyDTO {

    /**
     * 回复内容
     */
    @NotNull(message = "回复内容不能为空")
    private String content;

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    private Long reviewId;

    /**
     * 回复的上级回答id，没有可不填
     */
    private Long answerId;

    /**
     * 回复的目标回复id，没有可不填
     */
    private Long targetReplyId;

    /**
     * 回复的目标用户id，没有可不填
     */
    private Long targetUserId;

}
