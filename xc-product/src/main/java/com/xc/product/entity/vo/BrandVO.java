package com.xc.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class BrandVO {
    private Long brandId;

    @NotBlank
    private String brandName;

    /**
     * 图片id
     */
    @NotNull
    private Long imageId;
}
