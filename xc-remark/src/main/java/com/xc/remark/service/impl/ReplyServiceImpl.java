package com.xc.remark.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.constants.Constant;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.remark.domain.dto.ReplyDTO;
import com.xc.remark.domain.po.Reply;
import com.xc.remark.domain.po.Review;
import com.xc.remark.domain.query.ReplyPageQuery;
import com.xc.remark.domain.vo.ReplyVO;
import com.xc.remark.mapper.ReplyMapper;
import com.xc.remark.service.ILikedRecordService;
import com.xc.remark.service.IReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.remark.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 回复表 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements IReplyService {

    private final IReviewService reviewService;

    private final UserClient userClient;

    private final ILikedRecordService likedRecordService;

    @Override
    @Transactional
    public void saveReply(ReplyDTO replyDTO) {
        Long userId = UserContext.getUser();
        Reply reply = BeanUtils.copyBean(replyDTO, Reply.class);
        reply.setUserId(userId);
        save(reply);
        boolean isAnswer = reply.getAnswerId() == null;
        if(!isAnswer) {
            lambdaUpdate()
                    .eq(Reply::getId, reply.getAnswerId())
                    .setSql("reply_times = reply_times + 1")
                    .update();
        }

        reviewService.lambdaUpdate()
                .setSql("reply_times = reply_times + 1")
                .eq(Review::getId, replyDTO.getReviewId())
                .update();
    }

    @Override
    public PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery) {
        Long answerId = pageQuery.getAnswerId();
        Long reviewId = pageQuery.getReviewId();
        if(answerId == null && reviewId == null){
            throw new BadRequestException("评论和回答id不能同时为空");
        }
        boolean isReview = reviewId != null;
        Page<Reply> page = lambdaQuery()
                .eq(isReview, Reply::getReviewId, reviewId)
                .eq(Reply::getAnswerId, isReview ? 0L : answerId)
                .page(pageQuery.toMpPage(
                        new OrderItem(Constant.DATA_FIELD_NAME_LIKED_TIME, false),
                        new OrderItem(Constant.DATA_FIELD_NAME_CREATE_TIME, true)
                ));
        List<Reply> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> answerIds = new HashSet<>();
        Set<Long> targetReplyIds = new HashSet<>();
        for (Reply r : records) {
            userIds.add(r.getUserId());
            answerIds.add(r.getAnswerId());
            targetReplyIds.add(r.getTargetReplyId());
        }
        targetReplyIds.remove(0L);
        targetReplyIds.remove(null);
        if(CollUtils.isNotEmpty(targetReplyIds)){
            List<Reply> targetReplies = listByIds(targetReplyIds);
            Set<Long> targetUserIds = targetReplies.stream().map(Reply::getUserId).collect(Collectors.toSet());
            userIds.addAll(targetUserIds);
        }
        Map<Long, UserInfoResVO> userMap = new HashMap<>(userIds.size());
        if(CollUtils.isNotEmpty(userIds)){
            List<UserInfoResVO> userInfos = userClient.getUserInfos(userIds);
            if(CollUtils.isNotEmpty(userInfos)){
                userMap = userInfos.stream().collect(Collectors.toMap(UserInfoResVO::getUserId, u -> u));
            }
        }
        // 查询用户点赞状态
        Set<Long> bizLiked = likedRecordService.isBizLiked(answerIds);
        List<ReplyVO> voList = new ArrayList<>(records.size());
        for (Reply record : records) {
            ReplyVO vo = BeanUtils.copyBean(record, ReplyVO.class);
            UserInfoResVO user = userMap.get(record.getUserId());
            if(user != null){
                vo.setUserName(user.getAccount());
                vo.setUserIcon(user.getSrcface());
            }
            if(record.getTargetReplyId() != null){
                UserInfoResVO targetUser = userMap.get(record.getTargetReplyId());
                if(targetUser != null){
                    vo.setTargetUserName(targetUser.getNickName());
                }
            }
            vo.setLiked(bizLiked.contains(record.getId()));
            voList.add(vo);
        }
        return PageDTO.of(page, voList);
    }
}
