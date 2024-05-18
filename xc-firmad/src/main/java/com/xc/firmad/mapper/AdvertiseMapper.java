package com.xc.firmad.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 广告 Mapper 接口
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
@Mapper
public interface AdvertiseMapper extends BaseMapper<Advertise> {

}
