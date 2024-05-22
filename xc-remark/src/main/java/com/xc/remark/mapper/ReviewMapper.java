package com.xc.remark.mapper;

import com.xc.remark.domain.po.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
public interface ReviewMapper extends BaseMapper<Review> {

    void updateBatchByIds(@Param("list") List<Review> list);
}
