package com.xc.product.service;

import com.xc.product.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.vo.CategoryVO;

import java.util.LinkedList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface ICategoryService extends IService<Category> {
    boolean createCategory(CategoryVO vo);

    LinkedList<String> queryCategoryById(Long id, LinkedList<String> result);
}
