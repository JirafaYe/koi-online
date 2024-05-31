package com.xc.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("standard_product_unit")
public class StandardProductUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String spuName;

    private Long brandId;

    private Long categoryId;

    private Integer sales;

    /**
     * 图片
     */
    private String  mainImagesId;

    /**
     * 描述，json
     */
    private String content;

    /**
     * 详情图
     */
    private String  contentImagesId;

    /**
     * 商品最低价
     */
    private Integer minPrice;

    /**
     * 视频id
     */
    private Long mainVideoId;

    /**
     * 库存
     */
    private Long num;

    private boolean available;

    private Integer deleted;

    private Long creater;

    private Long updater;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
