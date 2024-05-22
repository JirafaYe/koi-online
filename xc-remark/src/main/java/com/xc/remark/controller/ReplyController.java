package com.xc.remark.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.remark.domain.dto.ReplyDTO;
import com.xc.remark.domain.query.ReplyPageQuery;
import com.xc.remark.domain.vo.ReplyVO;
import com.xc.remark.service.IReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 回复评论
 *
 * @author Koi
 * @since 2024-05-21
 */
@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final IReplyService replyService;

    /**
     * 新增回复
     * @param replyDTO
     */
    @PostMapping
    public void saveReply(@RequestBody ReplyDTO replyDTO){
        replyService.saveReply(replyDTO);
    }

    /**
     * 分页查询回复
     * @param pageQuery
     * @return
     */
    @GetMapping("/page")
    public PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery){
        return replyService.queryReplyPage(pageQuery);
    }
}
