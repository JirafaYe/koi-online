package com.xc.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.common.domain.dto.PageDTO;
import com.xc.log.entity.LogInfo;
import com.xc.log.vo.ListPageLogVO;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月20日
 */
public interface LogInfoService extends IService<LogInfo> {

    PageDTO<LogInfo> listPageLog(ListPageLogVO vo);
}
