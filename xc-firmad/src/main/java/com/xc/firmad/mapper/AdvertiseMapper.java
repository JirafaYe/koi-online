package com.xc.firmad.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.firmad.entity.Advertise;
import com.xc.firmad.vo.res.AdvertisePageResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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

    @Select("SELECT id, ad_name, ad_uri, file_id from advertise where ad_start_date <= #{now} AND ad_end_date >= #{now} ORDER BY RAND() LIMIT 1")
    Advertise getRandAdvertise(@Param("now") LocalDateTime now);
}
