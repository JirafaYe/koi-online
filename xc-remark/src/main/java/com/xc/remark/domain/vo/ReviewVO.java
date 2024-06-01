package com.xc.remark.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewVO {

    private Long id;

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
     * 是否点过赞
     */
    private Boolean liked;

    private LocalDateTime createTime;

    /**
     * 描述评分
     */
    private Integer descriptionScore;

    /**
     * 商品评分
     */
    private Integer productScore;

    /**
     * 是否有图片
     */
    private Boolean havePic;

    /**
     * 图片链接
     */
    private List<String> fileUrl;

    /**
     * 是否有视频
     */
    private Boolean haveVideo;

    /**
     * 视频链接
     */
    private String mediaUrl;

    private String userName;

    private String productName;


    /**
     * 所属spu name
     */
    private String  spuName;

    /**
     * 图片
     */
    private String image;

    /**
     * json储存的特征描述
     */
    private String attributes;
}
