package com.xc.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.dto.log.IogInfoReqDTO;
import com.xc.log.entity.LogInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月15日
 */
public interface LogInfoService extends IService<LogInfo> {
    /**
     * 保存错误日志
     * @param iogInfoReqDTO
     */
    void saveLog(IogInfoReqDTO iogInfoReqDTO);
}
