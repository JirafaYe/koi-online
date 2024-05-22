package com.xc.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.user.entity.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 用户基础信息表 Mapper 接口
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月11日
 */
@Mapper
public interface UserBaseMapper extends BaseMapper<UserBase> {

    @Update("UPDATE xc_user.user_base SET password = #{password} WHERE user_id = #{userId}")
    boolean updatePassword(@Param("password") String password, @Param("userId") Long userId);

    /**
     * 更新用户状态
     * @param ids
     * @return
     */
    boolean updateUserStatus(@Param("ids") List<Long> ids);


}
