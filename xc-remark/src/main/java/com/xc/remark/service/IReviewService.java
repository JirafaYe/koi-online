package com.xc.remark.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.remark.domain.dto.ReviewFormDTO;
import com.xc.remark.domain.po.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.remark.domain.query.ReviewPageQuery;
import com.xc.remark.domain.vo.ReviewVO;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
public interface IReviewService extends IService<Review> {

    void saveReview(ReviewFormDTO reviewDto);

    PageDTO<ReviewVO> queryReviewPage(ReviewPageQuery query);

    void deleteReview(Long id);
}
