package com.xc.trade.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xc.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundClassifyStatus implements BaseEnum {
    REFUND_ONLY(1, "仅退款"),
    RETURN_GOODS_REFUND(2, "退货退款");

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RefundClassifyStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (RefundClassifyStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        RefundClassifyStatus status = of(value);
        return status == null ? "" : status.desc;
    }
}
