package com.xc.firmad.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.firmad.entity.CorporatePartner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.firmad.vo.req.AddCorporatePartner;
import com.xc.firmad.vo.req.SearchCorporatePartnerVO;
import com.xc.firmad.vo.res.CorporatePartnerResVO;

import java.util.List;

/**
 * 合作企页表 服务类
 *
 * @author pengyalin
 * @since 2024年05月17日
 */
public interface CorporatePartnerService extends IService<CorporatePartner> {

    /**
     *分页查看所有合作企业
     */
    PageDTO<CorporatePartnerResVO> getCorporatePage(PageQuery vo);

    /**
     * 增加合作企业
     * @param vo
     * @return
     */
    boolean addCorporatePartner(AddCorporatePartner vo);

    /**
     * 删除合作企业
     * @param ids
     * @return
     */
    boolean deleteCorporatePartner(List<Integer> ids);

    /**
     * 模糊搜索合作企业
     * @param vo
     * @return
     */
    PageDTO<CorporatePartnerResVO> searchCorporatePartner(SearchCorporatePartnerVO vo);
}
