package com.xc.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频文件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {

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

    public static MediaDTO of(Long id, String filename, Float duration, Long size){
        return new MediaDTO(id, filename, duration, size);
    }
}
