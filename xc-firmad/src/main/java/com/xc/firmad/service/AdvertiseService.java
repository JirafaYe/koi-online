package com.xc.firmad.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.vo.req.AddAdvertise;
import com.xc.firmad.vo.res.AdvertisePageResVO;

/**
 * <p>
 * 广告 服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
public interface AdvertiseService extends IService<Advertise> {

    /**
     * 分页获取广告列表
     * @param vo
     * @return
     */
    PageDTO<AdvertisePageResVO> getAdvertisePage(PageQuery vo);

    Integer addAdvertise(AddAdvertise vo);
}
