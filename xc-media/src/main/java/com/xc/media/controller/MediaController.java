package com.xc.media.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.media.domain.dto.FileDTO;
import com.xc.media.domain.dto.MediaDTO;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.MediaVO;
import com.xc.media.service.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 *  视频上传等
 *
 * @author Koi
 * @since 2024-05-12
 */
@RestController
@RequestMapping("/medias")
@RequiredArgsConstructor
public class MediaController {

    private final IMediaService mediaService;

    /**
     * 分页查询视频
     * @return
     */
    @GetMapping("/list")
    public PageDTO<MediaVO> queryMediaPage(FileMediaQuery query){
        return mediaService.queryMediaPage(query);
    }

    /**
     * 上传视频
     * @param media
     * @return
     */
    @PostMapping("/upload")
    public MediaDTO uploadMedia(@RequestParam("media") MultipartFile media, @RequestParam("duration") Float duration){
        return mediaService.uploadMedia(media, duration);
    }

    /**
     * 查询指定视频信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public MediaDTO getMediaInfo(@PathVariable("id") Long id){
        return mediaService.getMediaInfo(id);
    }

    /**
     * 删除指定视频
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteMediaById(@PathVariable("id") Long id){
        mediaService.deleteMediaById(id);
    }

    /**
     * 批量删除视频
     * @param ids
     */
    @DeleteMapping("mediaIds")
    public void deleteMediaByIds(@RequestParam("ids") List<Long> ids){
        mediaService.deleteMediaByIds(ids);
    }

    /**
     * 暴露给其他服务调用，用于返回视频的信息
     * @param ids
     * @return
     */
    @GetMapping("/ids")
    public List<MediaDTO> getFileInfos(List<Long> ids) {
        return mediaService.getMediaInfos(ids);
    }
}
