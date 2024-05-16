package com.xc.product.entity.vo;

import com.xc.product.entity.Brand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BrandPageVO {
    public BrandPageVO(Brand brand, String creater, String updater) {
        log.info("creater->{},updater->{}",creater,updater);
        this.brandName=brand.getBrandName();
        this.id=brand.getId();
        this.imageId=brand.getImageId();
        this.creater = creater;
        this.updater = updater;
    }

    /**
     * 商品id
     */
    private Long id;

    private String brandName;

    /**
     * 图片链接
     */
    private Long  imageId;

    /**
     * 创建人用户名
     */
    private String creater;
    /**
     * 更新人用户名
     */
    private String updater;
}
