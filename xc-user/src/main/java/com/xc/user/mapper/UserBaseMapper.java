package com.xc.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.user.entity.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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

    @Update("UPDATE xc_user.user_base\n" +
            "SET password = 'new_password'\n" +
            "WHERE user_id = 'specific_user_id';")
    boolean updatePassword(String password, Long userId);
}
