package com.xc.product.entity.vo;

import com.xc.product.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
public class BrandPageVO {
    /**
     * 商品id
     */
    private Long id;

    private String brandName;

    /**
     * 图片链接
     */
    private String  fileUrl;

    /**
     * 创建人用户名
     */
    private String creater;
    /**
     * 更新人用户名
     */
    private String updater;
}
