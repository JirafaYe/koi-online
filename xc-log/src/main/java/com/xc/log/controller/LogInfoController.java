package com.xc.log.controller;


import com.xc.api.dto.log.IogInfoReqDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.UserContext;
import com.xc.log.entity.LogInfo;
import com.xc.log.service.LogInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.xc.common.constants.ErrorInfo.Msg.USER_NOT_EXISTS;

/**
 * <p>
 *  日志 前端控制器
 * </p>
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
     * 保存错误日志
     */
    @PostMapping("/saveLog")
    public void saveLog(IogInfoReqDTO iogInfoReqDTO){
      logInfoService.saveLog(iogInfoReqDTO);
    }


}

