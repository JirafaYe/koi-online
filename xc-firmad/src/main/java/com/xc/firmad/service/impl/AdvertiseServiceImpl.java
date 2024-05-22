package com.xc.firmad.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.mapper.AdvertiseMapper;
import com.xc.firmad.service.AdvertiseService;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.req.SearchAdvertiseVO;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        LambdaQueryWrapper<Advertise> lqw = new LambdaQueryWrapper<Advertise>().orderByAsc(Advertise::getAdStartDate);
        Page<Advertise> selectPage = lambdaQuery().like(StringUtils.isNotEmpty(vo.getAdName()), Advertise::getAdName, vo.getAdName())
                .between(vo.getMinExpense() != null && vo.getMaxExpense() != null, Advertise::getExpense, vo.getMinExpense(), vo.getMaxExpense())
                .gt(vo.getAdStartDate() != null, Advertise::getAdStartDate, vo.getAdStartDate())
                .lt(vo.getAdEndDate() != null, Advertise::getAdEndDate, vo.getAdEndDate())
                .orderByAsc(Advertise::getAdStartDate)
                .page(vo.toMpPage());
        List<Advertise> records = selectPage.getRecords();
        ArrayList<AdvertisePageResVO> list = new ArrayList<>();
        for (Advertise record : records) {
            String fileIds = record.getFileIds();
            AdvertisePageResVO resVO = new AdvertisePageResVO();
            List<Long> longIds = Arrays.stream(fileIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<FileDTO> fileInfos = mediaClient.getFileInfos(longIds);
            List<Long> collect = fileInfos.stream().map(FileDTO::getId).collect(Collectors.toList());
            BeanUtils.copyProperties(record, resVO);
            resVO.setFileIds(collect);
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
        String fileIds = vo.getFileIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        advertise.setFileIds(fileIds);
        return advertiseMapper.insert(advertise);
    }

    @Override
    public Integer deleteAdvertise(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
          return 0;
        }
        advertiseMapper.selectBatchIds(ids).forEach(advertise -> {
            String fileIds = advertise.getFileIds();
            List<Long> longIds = Arrays.stream(fileIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
//            TODO mediaClient.deleteFiles(longIds);
        });
        return advertiseMapper.deleteBatchIds(ids);
    }
}
