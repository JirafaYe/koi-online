package com.xc.remark.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 评论表
 *
 * @author Koi
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 点赞数量
     */
    private Integer likedTimes;

    /**
     * 是否有图片
     */
    private Boolean havePic;

    /**
     * 图片id
     */
    private String pics;

    /**
     * 是否有视频
     */
    private Boolean haveVideo;

    /**
     * 视频id
     */
    private Long video;

    /**
     * 描述评分
     */
    private Integer descriptionScore;

    /**
     * 商品评分
     */
    private Integer productScore;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
