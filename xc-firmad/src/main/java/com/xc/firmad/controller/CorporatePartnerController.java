package com.xc.firmad.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.firmad.service.CorporatePartnerService;
import com.xc.firmad.vo.req.AddCorporatePartner;
import com.xc.firmad.vo.req.FirmPageQuery;
import com.xc.firmad.vo.req.SearchCorporatePartnerVO;
import com.xc.firmad.vo.res.CorporatePartnerResVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 合作企页
 *
 * @author pengyalin
 * @since 2024年05月17日
 */
@RestController
@RequestMapping("/corporatePartner")
public class CorporatePartnerController {
    @Resource
    private CorporatePartnerService corporatePartnerService;

    /**
     * 分页查看所有合作企业
     * @return
     */
    @GetMapping("getCorporatePage")
    public PageDTO<CorporatePartnerResVO> getCorporatePage(FirmPageQuery vo){
        return corporatePartnerService.getCorporatePage(vo);
    }

    /**
     * 增加合作企业
     * @param vo
     * @return
     */
    @PostMapping("addCorporatePartner")
    public boolean addCorporatePartner(@RequestBody @Valid AddCorporatePartner vo){
        return corporatePartnerService.addCorporatePartner(vo);
    }

    /**
     * 删除合作企业
     * @param id
     * @return
     */
    @DeleteMapping("deleteCorporatePartner/{id}")
    public boolean deleteCorporatePartner(@PathVariable("id") Integer id){
        return corporatePartnerService.deleteCorporatePartner(id);
    }

}

