package com.xc.media.mapper;

import com.xc.media.domain.po.File;
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
public interface FileMapper extends BaseMapper<File> {

    @Select("select id, `key` from file where deleted = 1 AND update_time < #{ago}")
    List<File> queryUselessFile(@Param("ago") LocalDateTime ago);

    void deleteUselessFile(@Param("ids") List<Long> ids);
}
