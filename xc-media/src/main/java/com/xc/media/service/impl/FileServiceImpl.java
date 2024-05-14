package com.xc.media.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.exceptions.DbException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.common.utils.UserContext;
import com.xc.media.domain.dto.FileDTO;
import com.xc.media.domain.po.File;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.FileVO;
import com.xc.media.mapper.FileMapper;
import com.xc.media.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.media.storage.MinioFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.xc.media.constants.FileConstants.Msg.FILE_UPLOAD_ERROR;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final MinioFileStorage fileStorage;

    @Override
    public FileDTO uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = generateNewFileName(originalFilename);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e){
            throw new CommonException("文件读取异常", e);
        }
        String path = fileStorage.uploadFile(fileName, inputStream, file.getSize(), file.getContentType());
        File fileInfo = null;
        try {
            fileInfo = new File();
            fileInfo.setFilename(originalFilename);
            fileInfo.setKey(fileName);
            fileInfo.setFileUrl(path);
            save(fileInfo);
        } catch (Exception e) {
            log.error("文件信息保存异常", e);
            fileStorage.deleteFile(fileName);
            throw new DbException(FILE_UPLOAD_ERROR);
        }
        // 6.返回
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileInfo.getId());
        fileDTO.setPath(path);
        fileDTO.setFilename(originalFilename);
        return fileDTO;
    }

    @Override
    public FileDTO getFileInfo(Long id) {
        File file = getById(id);
        return file == null ? null : FileDTO.of(file.getId(), file.getFilename(), file.getFileUrl());
    }

    @Override
    public void deleteFileById(Long id) {
        removeById(id);
    }

    @Override
    public PageDTO<FileVO> queryFilePage(FileMediaQuery query) {
        Page<File> page = lambdaQuery()
                .like(StringUtils.isNotBlank(query.getName()), File::getFilename, query.getName())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<File> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        List<FileVO> list = BeanUtils.copyList(records, FileVO.class);
        return PageDTO.of(page, list);
    }

    @Override
    public void deleteFileByIds(List<Long> ids) {
        removeByIds(ids);
    }

    private String generateNewFileName(String originalFilename) {
        // 1.获取后缀
        String suffix = StringUtils.subAfter(originalFilename, ".", true);
        // 2.生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }
}
