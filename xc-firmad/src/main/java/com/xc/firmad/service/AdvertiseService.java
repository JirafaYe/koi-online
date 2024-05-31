package com.xc.firmad.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.common.domain.dto.PageDTO;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.req.SearchAdvertiseVO;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import com.xc.firmad.vo.res.AdvertiseVO;

import java.util.List;

/**
 * 广告 服务类
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
public interface AdvertiseService extends IService<Advertise> {

    /**
     * 分页获取广告列表 +
     * @param vo
     * @return
     */
    PageDTO<AdvertisePageResVO> getAdvertisePage(SearchAdvertiseVO vo);

    /**
     * 增加广告
     * @param vo
     * @return
     */
    Integer addAdvertise(AddAdvertise vo);

    /**
     * 删除广告
     * @param id
     * @return
     */
    Integer deleteAdvertise(Long id);

    AdvertiseVO userGetAdvertise();
}
