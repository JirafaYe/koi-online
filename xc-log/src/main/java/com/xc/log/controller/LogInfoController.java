package com.xc.log.controller;


import com.xc.api.dto.log.req.IogInfoReqDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.log.entity.LogInfo;
import com.xc.log.service.LogInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *  日志
 *
 * @author pengyalin
 * @since 2024年05月15日
 */
@RestController
@RequestMapping("/logInfo")
public class LogInfoController {

    @Resource
    private LogInfoService logInfoService;

    /**
     * 保存日志
     */
    @PostMapping("/saveLog")
    public void saveLog(@RequestBody IogInfoReqDTO iogInfoReqDTO){
      logInfoService.saveLog(iogInfoReqDTO);
    }


    /**
     * 展示日志
     */
    @GetMapping("/listPageLog")
    public PageDTO<LogInfo> listPageLog(@RequestBody @Valid PageQuery vo){
        return logInfoService.listPageLog(vo);
    }

}

