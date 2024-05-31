package com.xc.firmad.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.firmad.service.AdvertiseService;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.req.SearchAdvertiseVO;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import com.xc.firmad.vo.res.AdvertiseVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
     * 分页查询广告列表
     * @param vo
     * @return
     */
    @GetMapping("getAdvertisePage")
    public PageDTO<AdvertisePageResVO> getAdvertisePage(SearchAdvertiseVO vo) {
        return advertiseService.getAdvertisePage(vo);
    }

    /**
     * 添加广告
     * @param vo
     * @return
     */
    @PostMapping("addAdvertise")
    public Integer addAdvertise(@RequestBody @Valid AddAdvertise vo){
        return advertiseService.addAdvertise(vo);
    }

    /**
     * 删除广告
     * @param id
     * @return
     */
    @DeleteMapping("deleteAdvertise/{id}")
    public Integer deleteAdvertise(@PathVariable("id") Long id){
        return advertiseService.deleteAdvertise(id);
    }


    /**
     * 用户获取广告
     * @return
     */
    @GetMapping
    public AdvertiseVO userGetAdvertise(){
        return advertiseService.userGetAdvertise();
    }
}

