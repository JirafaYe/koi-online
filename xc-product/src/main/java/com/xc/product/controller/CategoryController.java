package com.xc.product.controller;

import com.xc.product.entity.vo.CategoryReqVO;
import com.xc.product.entity.vo.CategoryResVO;
import com.xc.product.entity.vo.OrderCategoryVO;
import com.xc.product.service.ICategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * 目录
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    ICategoryService categoryService;

    /**
     * 新增目录
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean createCategory(@Valid CategoryReqVO vo){
        return categoryService.createCategory(vo);
    }

    /**
     * 根据id查询
     * @param id 目录id
     * @return
     */
    @GetMapping("/{id}")
    public String queryCategoryById(@PathVariable Long id){
        LinkedList<String> names = new LinkedList<>();
        names=categoryService.queryCategoryById(id,names);
        return String.join("/",names);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @PostMapping ("/remove/{id}")
    public boolean removeCategoryById(@PathVariable Long id){
        return categoryService.removeCategory(id);
    }

    /**
     * 排序
     * @param vos
     * @return
     */
    @PostMapping ("/order")
    public boolean orderCategoryById(@Valid List<OrderCategoryVO> vos){
        return categoryService.orderCategory(vos);
    }

    /**
     * 查询所有目录
     * @return
     */
    @GetMapping("/all")
    public  List<CategoryResVO> queryAll(){
        return categoryService.queryCategories();
    }

    /**
     * 更新
     * @param vo
     * @return
     */
    @PostMapping ("/update")
    public boolean orderCategoryById(@Valid CategoryReqVO vo){
        return categoryService.update(vo);
    }
}
