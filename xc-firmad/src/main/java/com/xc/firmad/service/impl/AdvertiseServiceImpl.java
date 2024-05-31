package com.xc.firmad.service.impl;

import java.time.LocalDateTime;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.mapper.AdvertiseMapper;
import com.xc.firmad.service.AdvertiseService;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.req.SearchAdvertiseVO;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import com.xc.firmad.vo.res.AdvertiseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
@Service
public class AdvertiseServiceImpl extends ServiceImpl<AdvertiseMapper, Advertise> implements AdvertiseService {

    @Resource
    private AdvertiseMapper advertiseMapper;

    @Resource
    private MediaClient mediaClient;

    @Override
    public PageDTO<AdvertisePageResVO> getAdvertisePage(SearchAdvertiseVO vo) {
        Page<Advertise> selectPage = lambdaQuery().like(StringUtils.isNotEmpty(vo.getAdName()), Advertise::getAdName, vo.getAdName())
                .between(vo.getMinExpense() != null && vo.getMaxExpense() != null, Advertise::getExpense, vo.getMinExpense(), vo.getMaxExpense())
                .gt(vo.getAdStartDate() != null, Advertise::getAdStartDate, vo.getAdStartDate())
                .lt(vo.getAdEndDate() != null, Advertise::getAdEndDate, vo.getAdEndDate())
                .orderByAsc(Advertise::getAdStartDate)
                .page(vo.toMpPage());
        List<Advertise> records = selectPage.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(selectPage);
        }
        List<Long> imageIds = records.stream().map(Advertise::getFileId).collect(Collectors.toList());
        Map<Long, String> imageMap = mediaClient.getFileInfos(imageIds).stream().collect(Collectors.toMap(FileDTO::getId, FileDTO::getFileUrl));
        ArrayList<AdvertisePageResVO> list = new ArrayList<>(records.size());
        for (Advertise record : records) {
            AdvertisePageResVO resVO = new AdvertisePageResVO();
            BeanUtils.copyProperties(record, resVO);
            resVO.setFileUrl(imageMap.get(record.getFileId()));
            list.add(resVO);
        }

        return new PageDTO<AdvertisePageResVO>(selectPage.getTotal(), selectPage.getPages(), list);
    }

    @Override
    public Integer addAdvertise(AddAdvertise vo) {
        Advertise advertise = new Advertise();
        advertise.setAdName(vo.getAdName());
        advertise.setExpense(vo.getExpense());
        advertise.setAdStartDate(vo.getAdStartDate());
        advertise.setAdUri(vo.getAdUri());
        advertise.setAdEndDate(vo.getAdEndDate());
        List<FileDTO> fileInfos = mediaClient.getFileInfos(List.of(vo.getFileId()));
        if(CollUtils.isEmpty(fileInfos)){
            throw new BadRequestException("图片不存在");
        }
        advertise.setFileId(vo.getFileId());
        return advertiseMapper.insert(advertise);
    }

    @Override
    public Integer deleteAdvertise(Long id) {
        if (id == null) {
          return 0;
        }
        return advertiseMapper.deleteById(id);
    }

    @Override
    public AdvertiseVO userGetAdvertise() {
        LocalDateTime now = LocalDateTime.now();
        Advertise ad = baseMapper.getRandAdvertise(now);
        if(ad == null){
            return null;
        }
        FileDTO fileDTO = mediaClient.getFileInfos(List.of(ad.getFileId())).get(0);
        AdvertiseVO vo = BeanUtils.copyBean(ad, AdvertiseVO.class);
        vo.setImgUrl(fileDTO.getFileUrl());
        return vo;
    }
}
