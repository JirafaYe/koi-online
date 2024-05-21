package com.xc.media.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.enums.CommonStatus;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final UserClient userClient;

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
        fileDTO.setFileUrl(path);
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
        // 增加修改人
        List<Long> createrIds = records.stream().map(File::getCreater).collect(Collectors.toList());
        List<UserInfoResVO> userInfos = userClient.getUserInfos(createrIds);
        Map<Long, String> userNameMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfoResVO::getUserId, UserInfoResVO::getAccount));
        List<FileVO> list = records.stream().map(c -> {
            FileVO vo = BeanUtils.copyBean(c, FileVO.class);
            vo.setCreater(userNameMap.get(c.getCreater()));
            return vo;
        }).collect(Collectors.toList());
        return PageDTO.of(page, list);
    }

    @Override
    public void deleteFileByIds(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public List<String> queryAllUselessFile() {
        LocalDateTime time = LocalDateTime.now().minusDays(7L);
        List<File> list = baseMapper.queryUselessFile(time);
        if(CollUtils.isEmpty(list)){
            return CollUtils.emptyList();
        }
        List<Long> ids = list.stream().map(File::getId).collect(Collectors.toList());
        baseMapper.deleteUselessFile(ids);
        return list.stream().map(File::getKey).collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> getFileInfos(List<Long> ids) {
        if (CollUtils.isEmpty(ids)){
            return CollUtils.emptyList();
        }
        List<File> files = baseMapper.selectBatchIds(ids);
        return CollUtils.isEmpty(files) ? CollUtils.emptyList() : BeanUtils.copyList(files, FileDTO.class);
    }

    @Override
    public List<Long> judgeFileExist(List<Long> ids) {
        List<File> files = baseMapper.selectBatchIds(ids);
        return files.stream().map(File::getId).collect(Collectors.toList());
    }

    private String generateNewFileName(String originalFilename) {
        // 1.获取后缀
        String suffix = StringUtils.subAfter(originalFilename, ".", true);
        // 2.生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }
}
