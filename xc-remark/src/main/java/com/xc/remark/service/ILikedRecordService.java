package com.xc.remark.service;

import com.xc.remark.domain.dto.LikeTimesDTO;
import com.xc.remark.domain.dto.LikedRecordFormDTO;
import com.xc.remark.domain.po.LikedRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 点赞记录表 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-22
 */
public interface ILikedRecordService extends IService<LikedRecord> {

    void addLikeRecord(LikedRecordFormDTO recordDto);

    Set<Long> isBizLiked(Iterable<Long> bizIds);

    void readLikedTimes(String bizType, int maxBizSize);

    void updateLikedTimes(List<LikeTimesDTO> likedTimesDTOs, String bizType);
}
