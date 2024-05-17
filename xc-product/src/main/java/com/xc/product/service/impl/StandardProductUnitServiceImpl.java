package com.xc.product.service.impl;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.Brand;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.service.IStandardProductUnitService;
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
public class StandardProductUnitServiceImpl extends ServiceImpl<StandardProductUnitMapper, StandardProductUnit> implements IStandardProductUnitService {
    @Override
    public Integer countByBrand(Long brandId) {
        Integer integer = baseMapper.selectCount(lambdaQuery().eq(StandardProductUnit::getBrandId,brandId));
        return integer;
    }

    @Override
    public Integer countByCategory(Long id) {
        Integer integer = baseMapper.selectCount(lambdaQuery().eq(StandardProductUnit::getCategoryId,id));
        return integer;
    }

    @Override
    public boolean createSpu(SpuVO vo) {
        return false;
    }

    @Override
    public boolean removeSpu(Long id) {
        return false;
    }

    @Override
    public boolean updateSpu(SpuVO vo) {
        return false;
    }

    @Override
    public PageDTO<SpuPageVO> queryByPage(SpuQuery query) {
        return null;
    }
}
