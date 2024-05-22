package com.xc.remark.mapper;

import com.xc.remark.domain.po.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 回复表 Mapper 接口
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
public interface ReplyMapper extends BaseMapper<Reply> {

    void updateBatchByIds(List<Reply> list);
}
