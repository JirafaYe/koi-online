package com.xc.product.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResVO {
    private Long id;

    /**
     * 子目录
     */
    private List<CategoryResVO> children;

    /**
     * 目录名
     */
    private String categoryName;

    /**
     * 创键者
     */
    @JsonIgnore
    private Long creater;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 创建者名字
     */
    private String  createrName;
}
