package com.xc.media.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MediaVO {

    /**
     * 视频id
     */
    private Long id;

    /**
     * 视频名称
     */
    private String filename;

    /**
     * 视频时长
     */
    private Float duration;

    /**
     * 视频大小
     */
    private Long size;

    /**
     * 视频链接
     */
    private String mediaUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人id
     */
    private Long creater;
}
