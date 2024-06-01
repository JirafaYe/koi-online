package com.xc.remark.domain.dto;

import cn.hutool.log.Log;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增评论表单
 */
@Data
public class ReviewFormDTO {

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long productId;

    /**
     * 订单id
     */
    @NotNull(message = "订单详细id不能为空")
    private Long orderDetailId;

    /**
     * 描述
     */
    @NotNull(message = "描述不能为空")
    private String content;

    /**
     * 是否有图片
     */
    private Boolean havePic;

    /**
     * 图片ids
     */
    private List<Long> pics;

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
    @NotNull(message = "描述评分不能为空")
    @Max(value = 5)
    @Min(value = 1)
    private Integer descriptionScore;

    /**
     * 商品评分
     */
    @NotNull(message = "商品评分不能为空")
    @Max(value = 5)
    @Min(value = 1)
    private Integer productScore;

}
