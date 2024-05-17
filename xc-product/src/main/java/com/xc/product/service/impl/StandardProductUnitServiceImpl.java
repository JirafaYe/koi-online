package com.xc.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.product.entity.Brand;
import com.xc.product.entity.Category;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;
import com.xc.product.mapper.BrandMapper;
import com.xc.product.mapper.CategoryMapper;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.service.ICategoryService;
import com.xc.product.service.IStandardProductUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
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

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    BrandMapper brandMapper;

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
        List<Long> mainImages = splitImagesId(vo.getMainImagesId());
        List<Long> contentImages = splitImagesId(vo.getContentImagesId());
        //todo: 验证图片和视频真实性
        boolean res=false;
        if(!CollUtils.isEmpty(mainImages)&&!CollUtils.isEmpty(contentImages)
                &&ifCategoryExist(vo.getCategoryId())&&ifBrandExist(vo.getBrandId())){
            StandardProductUnit spu = BeanUtils.copyBean(vo, StandardProductUnit.class);
            res=save(spu);
        }
        return res;
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

    public List<Long> splitImagesId(String ids){
        String[] strings = StringUtils.splitToArray(ids, ",");
        int len= Math.min(strings.length, 8);
        LinkedList<Long> idList = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            idList.add(Long.valueOf(strings[i]));
        }
        return idList;
    }

    public boolean ifCategoryExist(Long id) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);

        return !categoryMapper.selectCount(wrapper).equals(0);
    }

    public boolean ifBrandExist(Long id) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);

        return !brandMapper.selectCount(wrapper).equals(0);
    }
}
