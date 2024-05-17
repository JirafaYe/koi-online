package com.xc.product.entity.vo;

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
     * 更新者
     */
    private Long creater;

    /**
     * 更新者名字
     */
    private String  createrName;
}
