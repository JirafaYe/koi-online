package com.xc.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xc.common.exceptions.BadRequestException;
import lombok.Getter;

@Getter
public enum UserType implements BaseEnum{
    USER(1, "用户"),
    ADMIN(2, "管理员");
    @EnumValue
    int value;
    String desc;

    UserType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserType of(int value) {
        for (UserType type : UserType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new BadRequestException("无效的用户类型");
    }
}
