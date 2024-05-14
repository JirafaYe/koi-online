package com.xc.media.job;


import com.xc.media.service.IFileService;
import com.xc.media.service.IMediaService;
import com.xc.media.storage.MinioFileStorage;
import com.xc.media.storage.MinioMediaStorage;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandleUselessFile {

    private final MinioFileStorage fileStorage;

    private final MinioMediaStorage mediaStorage;

    private final IFileService fileService;

    private final IMediaService mediaService;
    @XxlJob("deleteUselessFileAndMedia")
    public void deleteUselessFileAndMedia(){
        log.info("删除文件开始");
        List<String> files = fileService.queryAllUselessFile();
        List<String> medias = mediaService.queryAllUselessFile();
        fileStorage.deleteFiles(files);
        mediaStorage.deleteFiles(medias);
        log.info("删除文件结束");
    }
}
