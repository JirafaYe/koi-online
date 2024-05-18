package com.xc.firmad.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.MediaDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.utils.BeanUtils;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.mapper.AdvertiseMapper;
import com.xc.firmad.service.AdvertiseService;
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
    public PageDTO<AdvertisePageResVO> getAdvertisePage(PageQuery vo) {
        LambdaQueryWrapper<Advertise> lqw = new LambdaQueryWrapper<Advertise>().orderByAsc(Advertise::getAdStartDate);
        Page<Advertise> selectPage = advertiseMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), lqw);
        List<Advertise> records = selectPage.getRecords();
        ArrayList<AdvertisePageResVO> list = new ArrayList<>();
        for (Advertise record : records) {
            String fileIds = record.getFileIds();
            AdvertisePageResVO resVO = new AdvertisePageResVO();
            List<Long> longIds = Arrays.stream(fileIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<MediaDTO> fileInfos = mediaClient.getFileInfos(longIds);
            List<Long> collect = fileInfos.stream().map(MediaDTO::getId).collect(Collectors.toList());
            BeanUtils.copyProperties(record, resVO);
            resVO.setFileIds(collect);
            list.add(resVO);
        }

        return new PageDTO<AdvertisePageResVO>(selectPage.getTotal(), selectPage.getPages(), list);
    }
}
