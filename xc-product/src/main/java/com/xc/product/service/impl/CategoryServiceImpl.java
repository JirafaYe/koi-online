package com.xc.product.service.impl;

import com.xc.product.entity.Category;
import com.xc.product.mapper.CategoryMapper;
import com.xc.product.service.ICategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
