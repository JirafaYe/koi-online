package com.xc.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.dto.log.req.IogInfoReqDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
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
     */
    void saveLog(IogInfoReqDTO iogInfoReqDTO);

    /**
     * 展示日志
     * @param vo
     * @return
     */
    PageDTO<LogInfo> listPageLog(PageQuery vo);
}
