package com.xc.promotion.domain.dto;

import com.xc.common.validate.annotations.EnumValid;
import com.xc.promotion.domain.enums.DiscountType;
import com.xc.promotion.domain.enums.ObtainType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 优惠券新增表单数据
 */
@Data
public class CouponFormDTO {

    /**
     * 优惠券id，新增不需要添加，更新必填
     */
    private Long id;

    /**
     * 优惠券名称
     */
    @NotNull(message = "优惠券名称不能为空")
    @Size(max = 20, min = 4, message = "优惠券名称长度错误")
    private String name;

    /**
     * 是否添限定使用范围，true：限定了，false：没限定
     */
    private Boolean specific;

    /**
     * 优惠券使用范围
     */
    private List<Long> scopes;

    /**
     * 优惠券类型，1：每满减，2：折扣，3：无门槛，4：普通满减
     */
    @NotNull(message = "优惠券折扣类型不能为空")
    @EnumValid(enumeration = {1,2,3,4})
    private DiscountType discountType;

    /***
     * 折扣门槛，0代表无门槛
     */
    private Integer thresholdAmount;
    /**
     * 折扣值，满减填抵扣金额；打折填折扣值：80标示打8折
     */
    private Integer discountValue;
    /**
     * 最大优惠金额
     */
    private Integer maxDiscountAmount;

    /**
     * 优惠券总量
     */
    @Range(max = 5000, min = 1, message = "优惠券总量必须在1~5000")
    private Integer totalNum;
    /**
     * 每人领取的上限
     */
    @Range(max = 10, min = 1, message = "每人限领数量必须在1~10")
    private Integer userLimit;

    /***
     * 获取方式1：手动领取，2：指定发放（通过兑换码兑换）
     */
    @NotNull(message = "领取方式不能为空")
    @EnumValid(enumeration = {1, 2}, message = "领取方式不正确")
    private ObtainType obtainWay;
}