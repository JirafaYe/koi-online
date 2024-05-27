package com.xc.trade.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrdersStatus {
    AVAILABLE(1, "生效"),
    /**
     * 成功（收货后）
     */
    SUCCESS(2, "成功"),
    CLOSED(3, "已关闭"),
    PRE_REFUND(4, "退款申请"),
    REFUNDING(5, "退款中"),
    REFUNDED(6,"退款完成"),
    REFUNDED_REFUSED(7,"拒绝退款");
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrdersStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (OrdersStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        OrdersStatus status = of(value);
        return status == null ? "" : status.desc;
    }
}
