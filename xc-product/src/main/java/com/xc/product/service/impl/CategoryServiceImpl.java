package com.xc.product.service.impl;

import com.xc.product.entity.Category;
import com.xc.product.entity.vo.CategoryVO;
import com.xc.product.mapper.CategoryMapper;
import com.xc.product.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

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
    public boolean createCategory(CategoryVO vo) {
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

}
