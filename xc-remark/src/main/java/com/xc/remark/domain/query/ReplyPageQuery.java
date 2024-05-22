package com.xc.remark.domain.query;


import com.xc.common.domain.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 回复分页查询条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReplyPageQuery extends PageQuery {

    /**
     * 评论id，不为空则根据评论查询回复
     */
    private Long reviewId;

    /**
     * 回复id，不为空则根据回复查询回复
     */
    private Long answerId;
}
