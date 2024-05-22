package com.xc.remark.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 回复消息
 */
@Data
public class ReplyVO {

    private Long id;

    /**
     * 回答内容
     */
    private String content;
    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 创建时间，也就是回答时间
     */
    private LocalDateTime createTime;

    /**
     * 当前回复者id
     */
    private Long userId;

    /**
     * 当前回复者昵称
     */
    private String userName;

    /**
     * 当前回复者头像
     */
    private String userIcon;

    /**
     * 是否点过赞
     */
    private Boolean liked;

    /**
     * 点赞数量
     */
    private Integer likedTimes;

    /**
     * 目标用户名字
     */
    private String targetUserName;
}
