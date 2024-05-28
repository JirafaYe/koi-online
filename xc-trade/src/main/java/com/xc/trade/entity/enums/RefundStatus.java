package com.xc.trade.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xc.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundStatus implements BaseEnum {
    UN_APPROVE(1, "待审批"),
    CANCEL(2, "取消申请"),
    AGREE(3, "同意退款"),
    AGREE_RG(3, "同意退货"),
    WAIT_SENT_B(4, "待买家寄出"),
    WAIT_M_RECEIVE(5, "待商家收货"),
    REJECT(6, "拒绝退款"),
    SUCCESS(7, "退款成功"),
    FAILED(8, "退款失败");

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
