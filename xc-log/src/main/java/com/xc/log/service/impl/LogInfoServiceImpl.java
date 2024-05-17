package com.xc.log.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.log.req.IogInfoReqDTO;
import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.UserContext;
import com.xc.log.mapper.LogInfoMapper;
import com.xc.log.entity.LogInfo;
import com.xc.log.service.LogInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

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

        // 从用户模块获取了用户信息
        List<UserInfoResVO> userInfos = userClient.getUserInfos(Arrays.asList(userId));
        String method = iogInfoReqDTO.getMethod();
        String message = iogInfoReqDTO.getMessage();
        LogInfo logInfo = new LogInfo();
        logInfo.setUserId(userId);
        logInfo.setUserName(userInfos.get(0).getAccount());
        logInfo.setMethod(method);
        logInfo.setMessage(message);
        this.save(logInfo);
    }

    @Override
    public PageDTO<LogInfo> listPageLog(PageQuery vo) {

        Page<LogInfo> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        LambdaQueryWrapper<LogInfo> lqw = new LambdaQueryWrapper<LogInfo>().orderByDesc(LogInfo::getEventTime);
        Page<LogInfo> pageInfos = logInfoMapper.selectPage(page, lqw);
        PageDTO<LogInfo> ans = new PageDTO<>();
        ans.setList(pageInfos.getRecords());
        ans.setTotal(pageInfos.getTotal());
        return ans;
    }
}
