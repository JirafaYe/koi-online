package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SpuVO {
    /**
     * spu id
     */
    private Long id;

    @NotBlank
    private String spuName;

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    /**
     * 图片list,以，分隔,最多8个
     */
    @NotBlank
    private String  mainImagesId;

    /**
     * 描述
     */
    @NotBlank
    private String content;

    /**
     * 详情图，id以，分隔,最多8个
     */
    @NotBlank
    private String  contentImagesId;

    /**
     * 视频id
     */
    private Long mainVideoId;

    /**
     * 默认为false
     */
    private boolean available;
}
