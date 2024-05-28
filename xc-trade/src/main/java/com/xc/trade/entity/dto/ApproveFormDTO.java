package com.xc.trade.entity.dto;

import com.xc.common.validate.annotations.EnumValid;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 退款审批模型
 */
@Data
public class ApproveFormDTO{

    /**
     * 退款id
     */
    @NotNull(message = "退款id不能为空")
    private Long id;

    /**
     * 审批类型，1：同意，2：拒绝
     */
    @EnumValid(enumeration = {1,2}, message = "审批只有同意和拒绝两种操作")
    public Integer approveType;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 备注
     */
    private String remark;
}