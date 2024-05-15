package com.xc.product.service;

import com.xc.product.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
