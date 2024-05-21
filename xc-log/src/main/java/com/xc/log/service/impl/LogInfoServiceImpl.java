package com.xc.log.service.impl;


import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.log.entity.LogInfo;
import com.xc.log.mapper.LogInfoMapper;
import com.xc.log.service.LogInfoService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月20日
 */
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

    @Override
    public Boolean saveLog(LogInfo logInfo) {
     return false;



    }
}
