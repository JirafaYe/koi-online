package com.xc.remark.controller;


import com.xc.remark.domain.dto.LikedRecordFormDTO;
import com.xc.remark.service.ILikedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 *
 * 点赞记录接口
 *
 * @author Koi
 * @since 2024-05-22
 */
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikedRecordController {

    private final ILikedRecordService likedRecordService;

    /**
     * 点赞或者取消点赞
     * @param recordDto
     */
    @PostMapping
    public void addLikeRecord(@Valid @RequestBody LikedRecordFormDTO recordDto){
        likedRecordService.addLikeRecord(recordDto);
    }

    /**
     * 查询指定业务id的点赞状态
     * @param bizIds
     * @return
     */
    @GetMapping("/list")
    public Set<Long> isBizLiked(@RequestParam("bizIds") Iterable<Long> bizIds){
        return likedRecordService.isBizLiked(bizIds);
    }

}
