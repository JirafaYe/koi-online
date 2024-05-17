package com.xc.media.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图片信息
 */
@Data
public class FileVO {

    /**
     * 图片id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String filename;

    /**
     * 图片地址
     */
    private String fileUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人名称
     */
    private String creater;


}
