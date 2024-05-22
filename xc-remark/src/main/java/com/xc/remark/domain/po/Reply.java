package com.xc.remark.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 回复表
 *
 * @author Koi
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reply")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回答id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 评论id
     */
    private Long reviewId;


    /**
     * 回复的上级回答id
     */
    private Long answerId;

    /**
     * 回答者id
     */
    private Long userId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 回复的目标用户id
     */
    private Long targetUserId;

    /**
     * 回复的目标回复id
     */
    private Long targetReplyId;

    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 点赞数量
     */
    private Integer likedTimes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
