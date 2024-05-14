package com.xc.media.mapper;

import com.xc.media.domain.po.Media;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Koi
 * @since 2024-05-12
 */
public interface MediaMapper extends BaseMapper<Media> {
    @Select("select id, `key` from media where deleted = 1 AND update_time < #{ago}")
    List<Media> queryUselessMedia(@Param("ago") LocalDateTime ago);

    void deleteUselessMedia(@Param("ids") List<Long> ids);
}
