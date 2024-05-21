package com.xc.api.client.media;

import com.xc.api.client.media.fallback.MediaClientFallback;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "media-service", fallbackFactory = MediaClientFallback.class)
public interface MediaClient {

    /**
     * 暴露给其他服务调用，用于返回文件的信息
     * @param images
     * @return
     */
    @GetMapping("/files/images")
    List<FileDTO> getFileInfos(@RequestBody Iterable<Long> images);

    /**
     * 暴露给其他服务调用，用于返回视频的信息
     * @param videos
     * @return
     */
    @GetMapping("/medias/videos")
    List<MediaDTO> getMediaInfos(@RequestBody Iterable<Long> videos);


    /**
     * 判断文件是否存在
     * @param images
     * @return
     */
    @GetMapping("/files/exist")
    List<Long> judgeFileExist(@RequestBody Iterable<Long> images);

    /**
     * 判断视频是否存在
     * @param videos
     * @return
     */
    @GetMapping("/medias/exist")
    List<Long> judgeMediaExist(@RequestBody Iterable<Long> videos);
}
