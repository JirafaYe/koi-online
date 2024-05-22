package com.xc.log.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.log.entity.LogInfo;

import com.xc.log.mapper.LogInfoMapper;
import com.xc.log.service.LogInfoService;
import com.xc.log.vo.ListPageLogVO;
import com.xc.log.vo.LogInfoResVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


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

    @Resource
    private UserClient userClient;

    @Override
    public PageDTO<LogInfoResVO> listPageLog(ListPageLogVO vo) {
        String user = vo.getUser();
        List<Long> userIds = userClient.getUserListByName(user).stream().map(UserInfoResVO::getUserId).collect(Collectors.toList());
        Page<LogInfo> page = lambdaQuery()
                .like(CollUtils.isNotEmpty(userIds), LogInfo::getUserId, userIds)
                .like(StringUtils.isNotEmpty(vo.getTitle()), LogInfo::getTitle, vo.getTitle())
                .eq(vo.getOpStatus() != null, LogInfo::getStatus, vo.getOpStatus())
                .ge(vo.getOpStartTime() != null, LogInfo::getOperTime, vo.getOpStartTime())
                .le(vo.getOpEndTime() != null, LogInfo::getOperTime, vo.getOpEndTime())
                //业务类型通过前端下拉列表传递
                .eq(StringUtils.isNotEmpty(vo.getOpType()), LogInfo::getBusinessType, vo.getOpType())
                .orderByDesc(LogInfo::getOperTime)
                .page(vo.toMpPage());


        return null;

    }
}
