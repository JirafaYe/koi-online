package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderCategoryVO {
    /**
     * categoryId
     */
    @NotNull
    private Long id;

    /**
     * sequence of the category
     */
    @NotBlank
    private Integer sequence;
}
