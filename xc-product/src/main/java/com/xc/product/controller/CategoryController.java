package com.xc.product.controller;

import com.xc.product.entity.vo.CategoryVO;
import com.xc.product.service.ICategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.Stack;

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
    public boolean createCategory(@Valid CategoryVO vo){
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
}
