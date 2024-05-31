package com.xc.firmad.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.media.MediaClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.common.constants.ErrorInfo;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.JsonUtils;
import com.xc.common.utils.StringUtils;
import com.xc.firmad.entity.CorporatePartner;
import com.xc.firmad.mapper.CorporatePartnerMapper;
import com.xc.firmad.service.CorporatePartnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.firmad.vo.req.AddCorporatePartner;
import com.xc.firmad.vo.req.FirmPageQuery;
import com.xc.firmad.vo.req.SearchCorporatePartnerVO;
import com.xc.firmad.vo.res.CorporatePartnerResVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@RequiredArgsConstructor
public class CorporatePartnerServiceImpl extends ServiceImpl<CorporatePartnerMapper, CorporatePartner> implements CorporatePartnerService {

    @Resource
    private CorporatePartnerMapper corporatePartnerMapper;

    private final MediaClient mediaClient;

    @Override
    public PageDTO<CorporatePartnerResVO> getCorporatePage(FirmPageQuery query) {
        Page<CorporatePartner> page = lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getName()), CorporatePartner::getPartnerName, query.getName())
                .page(query.toMpPage());
        List<CorporatePartner> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        List<Long> imageIds = records.stream().map(CorporatePartner::getFileId).collect(Collectors.toList());
        Map<Long, String> imageMap = mediaClient.getFileInfos(imageIds).stream().collect(Collectors.toMap(FileDTO::getId, FileDTO::getFileUrl));
        List<CorporatePartnerResVO> voList = new ArrayList<>(records.size());
        for (CorporatePartner r : records) {
            CorporatePartnerResVO vo = BeanUtils.copyBean(r, CorporatePartnerResVO.class);
            String imageUrl = imageMap.get(r.getFileId());
            vo.setFileUrl(imageUrl);
            voList.add(vo);
        }
        return PageDTO.of(page, voList);
    }

    @Override
    public boolean addCorporatePartner(AddCorporatePartner vo) {

        CorporatePartner partner = new CorporatePartner();
        partner.setPartnerName(vo.getPartnerName());
        partner.setUriBrand(vo.getUriBrand());
        Long fileId = vo.getFileId();
        List<Long> list = mediaClient.judgeFileExist(List.of(fileId));
        if(CollUtils.isEmpty(list)){
            throw new BadRequestException("图片不存在");
        }
        partner.setFileId(fileId);
        partner.setRemark(vo.getRemark());
        return this.save(partner);
    }

    @Override
    public boolean deleteCorporatePartner(Integer id) {
        if(id == null){
            throw new CommonException(ErrorInfo.Code.FAILED,ErrorInfo.Msg.REQUEST_PARAM_ILLEGAL);
        }
        return corporatePartnerMapper.deleteById(id) != 0;

    }
}
