package com.xc.media.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.media.domain.dto.FileDTO;
import com.xc.media.domain.po.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.FileVO;
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
public interface IFileService extends IService<File> {

    FileDTO uploadFile(MultipartFile file);

    FileDTO getFileInfo(Long id);

    void deleteFileById(Long id);

    PageDTO<FileVO> queryFilePage(FileMediaQuery query);

    void deleteFileByIds(List<Long> ids);

    List<String> queryAllUselessFile();

    List<FileDTO> getFileInfos(List<Long> ids);
}
