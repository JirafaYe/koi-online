package com.xc.product.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.dto.BrandDTO;
import com.xc.product.entity.query.BrandQuery;
import com.xc.product.entity.vo.BrandPageVO;
import com.xc.product.entity.vo.BrandVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface IBrandService extends IService<Brand> {
   Boolean createBand(BrandVO vo);

   Boolean updateBrand(BrandVO vo);

   Boolean removeBrand(Long brandId);

   PageDTO<BrandPageVO> queryBrandsByPage(BrandQuery q);
}
