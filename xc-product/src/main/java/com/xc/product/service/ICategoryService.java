package com.xc.product.service;

import com.xc.product.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.vo.CategoryReqVO;
import com.xc.product.entity.vo.CategoryResVO;
import com.xc.product.entity.vo.OrderCategoryVO;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface ICategoryService extends IService<Category> {
    boolean createCategory(CategoryReqVO vo);

    LinkedList<String> queryCategoryById(Long id, LinkedList<String> result);

    boolean removeCategory(Long id);

    Boolean orderCategory(List<OrderCategoryVO> vos);

    List<CategoryResVO> queryCategories();
}
