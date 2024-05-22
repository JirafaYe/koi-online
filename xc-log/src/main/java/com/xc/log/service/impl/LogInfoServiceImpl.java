package com.xc.log.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.StringUtils;
import com.xc.log.entity.LogInfo;
import com.xc.log.mapper.LogInfoMapper;
import com.xc.log.service.LogInfoService;
import com.xc.log.vo.ListPageLogVO;
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
    public PageDTO<LogInfo> listPageLog(ListPageLogVO vo) {
        Page<LogInfo> page = lambdaQuery().like(StringUtils.isNotEmpty(vo.getUser()), LogInfo::getUserId, vo.getUser())
                .like(StringUtils.isNotEmpty(vo.getTitle()), LogInfo::getTitle, vo.getTitle())
                .eq(vo.getOpStatus() != null, LogInfo::getStatus, vo.getOpStatus())
                .ge(vo.getOpStartTime() != null, LogInfo::getOperTime, vo.getOpStartTime())
                .le(vo.getOpEndTime() != null, LogInfo::getOperTime, vo.getOpEndTime())
                .page(vo.toMpPage());
        String opType = vo.getOpType();


        return null;

    }
}
