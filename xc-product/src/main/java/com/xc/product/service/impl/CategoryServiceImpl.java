package com.xc.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.product.entity.Category;
import com.xc.product.entity.vo.CategoryReqVO;
import com.xc.product.entity.vo.CategoryResVO;
import com.xc.product.entity.vo.OrderCategoryVO;
import com.xc.product.mapper.CategoryMapper;
import com.xc.product.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Override
    public boolean createCategory(CategoryReqVO vo) {
        Category category = new Category();
        category.setCategoryName(vo.getName());
        if(vo.getParentId()!=null){
            category.setParentId(vo.getParentId());
        }
        return save(category);
    }

    @Override
    public LinkedList<String> queryCategoryById(Long id,LinkedList<String> names) {
        Category category = baseMapper.selectById(id);
        names.push(category.getCategoryName());
        if(category.getParentId()!=null){
            names=queryCategoryById(category.getParentId(),names);
        }
        return names;
    }

    //todo: 判断优惠券依赖
    @Override
    public boolean removeCategory(Long id) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        if(baseMapper.selectCount(wrapper)!=0){
            throw new CommonException("there are children belong to current category");
        }

        return removeById(id);
    }

    @Override
    public Boolean orderCategory(List<OrderCategoryVO> vos) {
        List<Category> categories = new LinkedList<>();
        for(OrderCategoryVO vo:vos){
            Category category = baseMapper.selectById(vo.getId());
            if(category!=null){
                category.setSequence(vo.getSequence());
                categories.add(category);
            }
        }
        boolean res=false;
        if(!CollUtils.isEmpty(categories)){
            res=updateBatchById(categories);
        }
        return res;
    }

    @Override
    public List<CategoryResVO> queryCategories() {
        List<CategoryResVO> categoryResVOS = queryCategories(new LinkedList<>(), true);
        return null;
    }

    public List<CategoryResVO> queryCategories(List<CategoryResVO> vos, boolean isFirst) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if(isFirst){
            wrapper.isNull("parent_id");
            wrapper.orderByAsc("sequence");
            List<Category> categories = baseMapper.selectList(wrapper);
            vos= BeanUtils.copyList(categories,CategoryResVO.class);
        }
        for(CategoryResVO cat:vos){
            wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id",cat.getId());
            wrapper.orderByAsc("sequence");
            List<Category> list = baseMapper.selectList(wrapper);
            if(!CollUtils.isEmpty(list)){
                List<CategoryResVO> intermedia = BeanUtils.copyList(list, CategoryResVO.class);
                intermedia=queryCategories(intermedia,false);
                cat.setChildren(intermedia);
            }
        }
        return vos;
    }
}
