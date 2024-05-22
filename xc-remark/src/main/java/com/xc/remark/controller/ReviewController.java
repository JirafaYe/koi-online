package com.xc.remark.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.remark.domain.dto.ReviewFormDTO;
import com.xc.remark.domain.query.ReviewPageQuery;
import com.xc.remark.domain.vo.ReviewVO;
import com.xc.remark.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * 评论接口
 *
 * @author Koi
 * @since 2024-05-21
 */
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    /**
     * 新增评论
     * @param reviewDto
     */
    @PostMapping
    public void saveReview(@Valid @RequestBody ReviewFormDTO reviewDto){
        reviewService.saveReview(reviewDto);
    }


    /**
     * 分页查询评论
     * @param query
     * @return
     */
    @GetMapping("/list")
    public PageDTO<ReviewVO> queryReviewPage(ReviewPageQuery query){
        return reviewService.queryReviewPage(query);
    }

    /**
     * 删除评论
     * @param id
     */
    @DeleteMapping("{id}")
    public void deleteReview(@PathVariable("id") Long id){
        reviewService.deleteReview(id);
    }
}
