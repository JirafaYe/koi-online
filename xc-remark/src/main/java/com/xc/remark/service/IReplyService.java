package com.xc.remark.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.remark.domain.dto.ReplyDTO;
import com.xc.remark.domain.po.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.remark.domain.query.ReplyPageQuery;
import com.xc.remark.domain.vo.ReplyVO;

import java.util.Collection;

/**
 * <p>
 * 回复表 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
public interface IReplyService extends IService<Reply> {

    void saveReply(ReplyDTO replyDTO);

    PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery);
}
