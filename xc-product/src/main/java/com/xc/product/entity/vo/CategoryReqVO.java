package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryReqVO {
    /**
     * 父目录，为空则为一级目录
     */
    private Long parentId;

    /**
     * 目录名
     */
    @NotBlank
    private String name;
}
