package com.xc.remark.task;

import com.xc.remark.service.ILikedRecordService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikedTimesCheckTask {
    private static final List<String> BIZ_TYPES = List.of("REVIEW", "REPLY");

    private final ILikedRecordService likedRecordService;

    private static final int MAX_BIZ_SIZE = 30;

    @XxlJob("checkLikedTimes")
    public void  checkLikedTimes(){
        log.debug("点赞数写入数据库任务启动");
        for (String bizType : BIZ_TYPES) {
            likedRecordService.readLikedTimes(bizType, MAX_BIZ_SIZE);
        }
        log.debug("点赞数写入数据库任务结束");
    }
}
