package com.xc.remark.service.impl;

import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.remark.constants.RedisConstants;
import com.xc.remark.domain.dto.LikeTimesDTO;
import com.xc.remark.domain.dto.LikedRecordFormDTO;
import com.xc.remark.domain.po.LikedRecord;
import com.xc.remark.domain.po.Reply;
import com.xc.remark.domain.po.Review;
import com.xc.remark.mapper.LikedRecordMapper;
import com.xc.remark.mapper.ReplyMapper;
import com.xc.remark.mapper.ReviewMapper;
import com.xc.remark.service.ILikedRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.remark.service.IReplyService;
import com.xc.remark.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * <p>
 * 点赞记录表 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-22
 */
@Service
@RequiredArgsConstructor
public class LikedRecordServiceImpl extends ServiceImpl<LikedRecordMapper, LikedRecord> implements ILikedRecordService {

    private final StringRedisTemplate redisTemplate;

    private final ReplyMapper replyMapper;

    private final ReviewMapper reviewMapper;

    @Override
    public void addLikeRecord(LikedRecordFormDTO recordDto) {
        boolean success = recordDto.getLiked() ? like(recordDto) : unlike(recordDto);
        if(!success){
            throw new BizIllegalException("点赞或取消点赞失败");
        }
        Long likeTimes = redisTemplate.opsForSet().size(RedisConstants.LIKES_BIZ_KEY_PREFIX + recordDto.getBizId());
        if(likeTimes == null){
            return;
        }
        redisTemplate.opsForZSet().add(
                RedisConstants.LIKES_TIMES_KEY_PREFIX + recordDto.getBizType(),
                recordDto.getBizId().toString(),
                likeTimes
        );
    }

    @Override
    public Set<Long> isBizLiked(Iterable<Long> bizIds) {
        Long userId = UserContext.getUser();
        List<Object> objects = redisTemplate.executePipelined((RedisCallback<?>) connection -> {
            for (Long bizId : bizIds) {
                String key = RedisConstants.LIKES_BIZ_KEY_PREFIX + bizId;
                //results.add(src.sIsMember(key, userId.toString()));
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] userIdBytes = userId.toString().getBytes(StandardCharsets.UTF_8);
                connection.sIsMember(keyBytes, userIdBytes);
            }
            return null;
        });
        return IntStream.range(0, objects.size())
                .filter(i -> (boolean) objects.get(i))
                .mapToObj(StreamSupport.stream(bizIds.spliterator(), false)
                        .collect(Collectors.toList())::get)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void readLikedTimes(String bizType, int maxBizSize) {
        String key = RedisConstants.LIKES_TIMES_KEY_PREFIX + bizType;
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().popMin(key, maxBizSize);
        if(CollUtils.isEmpty(tuples)){
            return;
        }
        List<LikeTimesDTO> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String bizId = tuple.getValue();
            Double likedTimes = tuple.getScore();
            if(bizId == null || likedTimes == null){
                continue;
            }
            list.add(LikeTimesDTO.of(Long.valueOf(bizId), likedTimes.intValue()));
        }
        updateLikedTimes(list, bizType);
    }

    @Async
    @Override
    public void updateLikedTimes(List<LikeTimesDTO> likedTimesDTOs, String bizType) {
        if(bizType.equals("REVIEW")){
            List<Review> list = new ArrayList<>(likedTimesDTOs.size());
            for (LikeTimesDTO dto : likedTimesDTOs) {
                Review r = new Review();
                r.setId(dto.getBizId());
                r.setLikedTimes (dto.getLikeTimes());
                list.add(r);
            }
            reviewMapper.updateBatchByIds(list);
        }else if(bizType.equals("REPLY")){
            List<Reply> list = new ArrayList<>(likedTimesDTOs.size());
            for (LikeTimesDTO dto : likedTimesDTOs) {
                Reply r = new Reply();
                r.setId(dto.getBizId());
                r.setLikedTimes(dto.getLikeTimes());
                list.add(r);
            }
            replyMapper.updateBatchByIds(list);
        }

    }

    private boolean unlike(LikedRecordFormDTO recordDto) {
        Long userId = UserContext.getUser();
        String key = RedisConstants.LIKES_BIZ_KEY_PREFIX + recordDto.getBizId();
        Long res = redisTemplate.opsForSet().remove(key, userId.toString());
        return  res != null && res > 0;
    }

    private boolean like(LikedRecordFormDTO recordDto) {
        Long userId = UserContext.getUser();
        String key = RedisConstants.LIKES_BIZ_KEY_PREFIX + recordDto.getBizId();
        Long res = redisTemplate.opsForSet().add(key, userId.toString());
        return  res != null && res > 0;
    }
}
