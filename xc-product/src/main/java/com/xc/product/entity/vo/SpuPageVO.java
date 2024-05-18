package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SpuPageVO {
    private Long id;

    /**
     * 商品名
     */
    private String spuName;

    /**
     * 品牌
     */
    private String  brand;

    /**
     * 分类
     */
    private String  category;

    /**
     * 图片list,以，分隔,最多8个
     */
    private String  mainImagesId;

    /**
     * 描述
     */
    private String content;

    /**
     * 详情图，id以，分隔,最多8个
     */
    private String  contentImagesId;

    /**
     * 视频id
     */
    private Long mainVideoId;

    /**
     * 默认为false
     */
    private Boolean available;

    /**
     * 库存
     */
    private Long num;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 更新人
     */
    private String updater;
}
