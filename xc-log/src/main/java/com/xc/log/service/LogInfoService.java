package com.xc.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.log.entity.LogInfo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月20日
 */
public interface LogInfoService extends IService<LogInfo> {

    /**
     *
     * @param logInfo
     * @return
     */
    Boolean saveLog(LogInfo logInfo);
}
