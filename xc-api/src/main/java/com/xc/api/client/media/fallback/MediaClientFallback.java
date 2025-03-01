package com.xc.api.client.media.fallback;

import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import com.xc.common.utils.CollUtils;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class MediaClientFallback implements FallbackFactory<MediaClient> {
    @Override
    public MediaClient create(Throwable cause) {
        return new MediaClient() {
            @Override
            public List<FileDTO> getFileInfos(Iterable<Long> images) {
                return CollUtils.emptyList();
            }

            @Override
            public List<MediaDTO> getMediaInfos(Iterable<Long> videos) {
                return CollUtils.emptyList();
            }

            @Override
            public List<Long> judgeFileExist(Iterable<Long> images) {
                return CollUtils.emptyList();
            }

            @Override
            public List<Long> judgeMediaExist(Iterable<Long> videos) {
                return CollUtils.emptyList();
            }
        };
    }
}
