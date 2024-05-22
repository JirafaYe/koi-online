package com.xc.log.controller;


import cn.hutool.core.convert.Convert;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.log.aspect.Log;
import com.xc.log.entity.LogInfo;
import com.xc.log.enums.BusinessType;
import com.xc.log.service.LogInfoService;
import com.xc.log.vo.ListPageLogVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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
    public Boolean saveLog(@RequestBody LogInfo logInfo){
        return logInfoService.save(logInfo);
    }


    /**
     * 展示日志
     */
    @GetMapping("/listPageLog")
    public PageDTO<LogInfo> listPageLog(ListPageLogVO vo){
        return logInfoService.listPageLog(vo);
    }


}

