package com.xc.product.service.impl;

import com.xc.product.entity.Brand;
import com.xc.product.entity.vo.BrandVO;
import com.xc.product.mapper.BrandMapper;
import com.xc.product.service.IBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Override
    public Boolean createBand(BrandVO vo) {
        Brand brand = new Brand();
        brand.setImageId(vo.getImageId());
        brand.setBrandName(vo.getBrandName());

        return save(brand);
    }
}
