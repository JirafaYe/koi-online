package com.xc.media.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.enums.CommonStatus;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.exceptions.DbException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.media.domain.dto.MediaDTO;
import com.xc.media.domain.po.File;
import com.xc.media.domain.po.Media;
import com.xc.media.domain.query.FileMediaQuery;
import com.xc.media.domain.vo.FileVO;
import com.xc.media.domain.vo.MediaVO;
import com.xc.media.mapper.MediaMapper;
import com.xc.media.service.IMediaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.media.storage.MinioMediaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xc.media.constants.FileConstants.Msg.MEDIA_UPLOAD_ERROR;

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
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    private final MinioMediaStorage mediaStorage;

    private final UserClient userClient;

    @Override
    public MediaDTO uploadMedia(MultipartFile media, Float duration) {
        if(duration * 100 > 3000){
            throw new BizIllegalException("视频最多为30秒");
        }
        String originalFilename = media.getOriginalFilename();
        String fileName = generateNewFileName(originalFilename);
        long size = media.getSize();
        InputStream inputStream;
        try {
            inputStream = media.getInputStream();
        } catch (IOException e){
            throw new CommonException("视频读取异常", e);
        }
        String path = mediaStorage.uploadFile(fileName, inputStream, size, media.getContentType());
        Media mediaInfo = null;
        try {
            mediaInfo = new Media();
            mediaInfo.setFilename(originalFilename);
            mediaInfo.setMediaUrl(path);
            mediaInfo.setKey(fileName);
            mediaInfo.setDuration(duration);
            mediaInfo.setSize(size);
            save(mediaInfo);
        } catch (Exception e) {
            log.error("视频信息保存异常", e);
            mediaStorage.deleteFile(fileName);
            throw new DbException(MEDIA_UPLOAD_ERROR);
        }
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setId(mediaInfo.getId());
        mediaDTO.setDuration(duration);
        mediaDTO.setSize(size);
        mediaDTO.setFilename(originalFilename);
        return mediaDTO;
    }

    @Override
    public PageDTO<MediaVO> queryMediaPage(FileMediaQuery query) {
        Page<Media> page = lambdaQuery()
                .like(StringUtils.isNotBlank(query.getName()), Media::getFilename, query.getName())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<Media> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        // 增加创建人
        List<Long> createrIds = records.stream().map(Media::getCreater).collect(Collectors.toList());
        List<UserInfoResVO> userInfos = userClient.getUserInfos(createrIds);
        Map<Long, String> userNameMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfoResVO::getUserId, UserInfoResVO::getAccount));
        List<MediaVO> list = records.stream().map(c -> {
            MediaVO vo = BeanUtils.copyBean(c, MediaVO.class);
            vo.setCreater(userNameMap.get(c.getCreater()));
            return vo;
        }).collect(Collectors.toList());
        return PageDTO.of(page, list);
    }

    @Override
    public MediaDTO getMediaInfo(Long id) {
        Media media = getById(id);
        return media == null ? null : MediaDTO.of(media.getId(), media.getFilename(), media.getDuration(), media.getSize());
    }

    @Override
    public void deleteMediaById(Long id) {
        removeById(id);
    }

    @Override
    public void deleteMediaByIds(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public List<String> queryAllUselessFile() {
        LocalDateTime time = LocalDateTime.now().minusDays(7L);
        List<Media> list = baseMapper.queryUselessMedia(time);
        if(CollUtils.isEmpty(list)){
            return CollUtils.emptyList();
        }
        List<Long> ids = list.stream().map(Media::getId).collect(Collectors.toList());
        baseMapper.deleteUselessMedia(ids);
        return list.stream().map(Media::getKey).collect(Collectors.toList());
    }

    @Override
    public List<MediaDTO> getMediaInfos(List<Long> ids) {
        if (CollUtils.isEmpty(ids)){
            return CollUtils.emptyList();
        }
        List<Media> media = baseMapper.selectBatchIds(ids);
        return CollUtils.isEmpty(media) ? CollUtils.emptyList() : BeanUtils.copyList(media, MediaDTO.class);
    }

    @Override
    public List<Long> judgeMediaExist(List<Long> ids) {
        List<Media> media = baseMapper.selectBatchIds(ids);
        return media.stream().map(Media::getId).collect(Collectors.toList());
    }

    private String generateNewFileName(String originalFilename) {
        // 1.获取后缀
        String suffix = StringUtils.subAfter(originalFilename, ".", true);
        // 2.生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }
}
