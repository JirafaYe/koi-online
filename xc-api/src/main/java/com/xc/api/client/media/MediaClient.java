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
     * @param ids
     * @return
     */
    @GetMapping("/files/ids")
    List<FileDTO> getFileInfos(Iterable<Long> ids);

    /**
     * 暴露给其他服务调用，用于返回视频的信息
     * @param ids
     * @return
     */
    @GetMapping("/medias/ids")
    List<MediaDTO> getFileInfos(List<Long> ids);


    /**
     * 判断文件是否存在
     * @param ids
     * @return
     */
    @GetMapping("/files/exist")
    List<Long> judgeFileExist(@RequestBody Iterable<Long> ids);

    /**
     * 判断视频是否存在
     * @param ids
     * @return
     */
    @GetMapping("/medias/exist")
    List<Long> judgeMediaExist(@RequestBody Iterable<Long> ids);
}
