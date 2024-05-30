package com.xc.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SkuListVO {
    /**
     * 所属spu id
     */
    @NotNull
    private Long spuId;

    /**
     * spu下sku列表
     */
    private List<SkuDetailsVO> vos;
}
