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
     * 商品最低价
     */
    private Integer minPrice;

    /**
     * 品牌
     */
    private String  brand;

    /**
     * 分类
     */
    private String  category;

    /**
     * 图片url list,以，分隔,最多8个
     */
    private String  mainImagesUrl;

    /**
     * 描述
     */
    private String content;

    /**
     * 详情图，url以，分隔,最多8个
     */
    private String  contentImagesUrl;

    /**
     * 视频url
     */
    private String  mainVideoUrl;

    /**
     * 默认为false
     */
    private Boolean available;

    /**
     * 库存
     */
    private Long num;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 创建人
     */
    private String creater;


    /**
     * 是否新品（7天内创建）
     */
    private boolean upToDate=false;
}
