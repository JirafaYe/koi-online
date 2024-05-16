package com.xc.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.user.UserClient;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.product.entity.Brand;
import com.xc.product.entity.dto.BrandDTO;
import com.xc.product.entity.query.BrandQuery;
import com.xc.product.entity.vo.BrandVO;
import com.xc.product.mapper.BrandMapper;
import com.xc.product.service.IBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.product.service.IStandardProductUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    IStandardProductUnitService spuService;

    @Resource
    UserClient userClient;

    @Override
    public Boolean updateBrand(BrandVO vo) {
        Long brandId = vo.getBrandId();
        if(brandId==null||brandId==0){
            throw new CommonException("required brandId value");
        }
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setImageId(vo.getImageId());
        brand.setBrandName(vo.getBrandName());

        return updateById(brand);
    }

    @Override
    public Boolean createBand(BrandVO vo) {
        Brand brand = new Brand();
        brand.setImageId(vo.getImageId());
        brand.setBrandName(vo.getBrandName());

        return save(brand);
    }

    @Override
    public Boolean removeBrand(Long brandId) {
        if(brandId==null||brandId==0) {
            throw new CommonException("required brandId value");
        }
        boolean ret=false;
        if(spuService.countByBrand(brandId)==0){
            ret=removeById(brandId);
        }
        return ret;
    }

    //todo: convert userId to userName
    @Override
    public PageDTO<BrandDTO> queryBrandsByPage(BrandQuery q) {
        Page<Brand> page = lambdaQuery().page(q.toMpPageDefaultSortByCreateTimeDesc());
        PageDTO<BrandDTO> result = PageDTO.empty(page);
        List<Brand> records = page.getRecords();
        if(!CollUtils.isEmpty(records)){
            List<Long> userIds = records.stream().map(Brand::getCreater).collect(Collectors.toList());
            userIds.addAll(records.stream().map(Brand::getUpdater).collect(Collectors.toList()));

            HashMap<Long, String> userMap = new HashMap<>();
            for(Long id:userIds){
                if(!userMap.containsKey(id)){

                }
            }

            result=PageDTO.of(page, BeanUtils.copyList(page.getRecords(), BrandDTO.class));
        }
        return result;
    }
}
