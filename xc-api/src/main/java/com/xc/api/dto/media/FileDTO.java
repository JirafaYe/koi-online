package com.xc.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    /**
     * 文件id
     */
    private Long id;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件访问路径
     */
    private String path;

    public static FileDTO of(Long id, String filename, String path){
        return new FileDTO(id, filename, path);
    }
}
