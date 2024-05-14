package com.xc.media.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.media.domain.dto.MediaDTO;
import com.xc.media.domain.po.Media;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.MediaVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-12
 */
public interface IMediaService extends IService<Media> {

    MediaDTO uploadMedia(MultipartFile media, Float duration);

    PageDTO<MediaVO> queryMediaPage(FileMediaQuery query);

    MediaDTO getMediaInfo(Long id);

    void deleteMediaById(Long id);

    void deleteMediaByIds(List<Long> ids);

    List<String> queryAllUselessFile();

    List<MediaDTO> getMediaInfos(List<Long> ids);
}
