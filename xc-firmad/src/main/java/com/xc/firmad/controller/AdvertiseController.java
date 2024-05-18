package com.xc.firmad.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.firmad.service.AdvertiseService;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 广告 前端控制器
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
@RestController
@RequestMapping("/advertise")
public class AdvertiseController {

    @Resource
    private AdvertiseService advertiseService;

    /**
     * 分页获取广告列表
     * @param vo
     * @return
     */
    @GetMapping("getAdvertisePage")
    public PageDTO<AdvertisePageResVO> getAdvertisePage(PageQuery vo) {
        return advertiseService.getAdvertisePage(vo);
    }

    /**
     * 添加广告
     * @param vo
     * @return
     */
    @PostMapping("addAdvertise")
    public Integer addAdvertise(@RequestBody AddAdvertise vo){
        return advertiseService.addAdvertise(vo);
    }
}

