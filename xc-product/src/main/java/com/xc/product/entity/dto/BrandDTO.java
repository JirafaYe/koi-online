package com.xc.product.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xc.common.domain.dto.BaseDTO;
import lombok.Data;

@Data
public class BrandDTO extends BaseDTO {
    /**
     * 商品id
     */
    private Long id;

    private String brandName;

    /**
     * 图片链接
     */
    private String  image;
}
