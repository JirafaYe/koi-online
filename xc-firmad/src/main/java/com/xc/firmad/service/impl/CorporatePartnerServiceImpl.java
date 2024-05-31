package com.xc.firmad.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.common.constants.ErrorInfo;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.JsonUtils;
import com.xc.firmad.entity.CorporatePartner;
import com.xc.firmad.mapper.CorporatePartnerMapper;
import com.xc.firmad.service.CorporatePartnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.firmad.vo.req.AddCorporatePartner;
import com.xc.firmad.vo.req.SearchCorporatePartnerVO;
import com.xc.firmad.vo.res.CorporatePartnerResVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 合作企页表 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月17日
 */
@Service
public class CorporatePartnerServiceImpl extends ServiceImpl<CorporatePartnerMapper, CorporatePartner> implements CorporatePartnerService {

    @Resource
    private CorporatePartnerMapper corporatePartnerMapper;
    @Resource
    private MediaClient mediaClient;
    @Override
    public PageDTO<CorporatePartnerResVO> getCorporatePage(PageQuery vo) {

        PageMethod.startPage(vo.getPageNo(), vo.getPageSize());
        List<CorporatePartnerResVO> list =  corporatePartnerMapper.getCorporatePage();
        PageInfo<CorporatePartnerResVO> pageInfo = new PageInfo<>(list);
        return  new PageDTO<>(pageInfo.getTotal(), (long) pageInfo.getPages(),list);

    }

    @Override
    public boolean addCorporatePartner(AddCorporatePartner vo) {

        CorporatePartner partner = new CorporatePartner();
        partner.setPartnerName(vo.getPartnerName());
        partner.setUriBrand(vo.getUriBrand());
        Long fileId = vo.getFileId();
        //判断文件是否存在
        List<FileDTO> fileInfos = mediaClient.getFileInfos(List.of(fileId));
        if(CollUtil.isEmpty(fileInfos)){
            throw new CommonException(ErrorInfo.Code.FAILED,ErrorInfo.Msg.FILE_NOT_EXIST);
        }
        partner.setFileId(fileId);
        partner.setRemark(vo.getRemark());
        return this.save(partner);
    }

    @Override
    public boolean deleteCorporatePartner(List<Integer> ids) {
        if(CollUtil.isEmpty(ids)){
            throw new CommonException(ErrorInfo.Code.FAILED,ErrorInfo.Msg.REQUEST_PARAM_ILLEGAL);
        }
        return corporatePartnerMapper.deleteBatchIds(ids) != 0;

    }

    @Override
    public PageDTO<CorporatePartnerResVO> searchCorporatePartner(SearchCorporatePartnerVO vo) {
        LambdaQueryWrapper<CorporatePartner> lqw = new LambdaQueryWrapper<CorporatePartner>()
                .like(CorporatePartner::getPartnerName, vo.getName());
        Page<CorporatePartner> page = corporatePartnerMapper.selectPage(new Page<>(vo.getPageNo(), vo.getPageSize()), lqw);
        List<CorporatePartnerResVO> list = page.getRecords().stream()
                .map(record -> {
                    CorporatePartnerResVO resVO = new CorporatePartnerResVO();
                    BeanUtils.copyProperties(record, resVO);
                    return resVO;
                })
                .collect(Collectors.toList());
        return new PageDTO<>(page.getTotal(),page.getPages(),list);
    }
}
