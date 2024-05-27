package com.xc.trade.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundStatus {
    PRE_REFUND(1, "退款申请"),
    REFUNDING(2, "退款中"),
    REFUNDED(3,"退款完成"),
    REFUNDED_REFUSED(4,"拒绝退款");
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RefundStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (RefundStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        RefundStatus status = of(value);
        return status == null ? "" : status.desc;
    }
}
