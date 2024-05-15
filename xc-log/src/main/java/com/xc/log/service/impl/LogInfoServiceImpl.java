package com.xc.log.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.log.IogInfoReqDTO;
import com.xc.common.domain.dto.CommonLongIdDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.UserContext;
import com.xc.log.mapper.LogInfoMapper;
import com.xc.log.entity.LogInfo;
import com.xc.log.service.LogInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.xc.common.constants.ErrorInfo.Msg.USER_NOT_EXISTS;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月15日
 */
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

    @Resource
    private LogInfoMapper logInfoMapper;
    @Resource
    private UserClient userClient;

    @Override
    public void saveLog(IogInfoReqDTO iogInfoReqDTO) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommonException(USER_NOT_EXISTS);
        }
        CommonLongIdDTO vo = new CommonLongIdDTO();
        vo.setId(userId);
        //TODO 我们从用户模块获取了用户信息
        userClient.getUserInfo(vo);
        String method = iogInfoReqDTO.getMethod();
        String message = iogInfoReqDTO.getMessage();
        LogInfo logInfo = new LogInfo();
        logInfo.setUserId(userId);
        logInfo.setMethod(method);
        logInfo.setMessage(message);
        logInfoMapper.updateById(logInfo);

    }
}
