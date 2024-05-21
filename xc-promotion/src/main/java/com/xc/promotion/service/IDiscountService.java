package com.xc.promotion.service;

import com.xc.promotion.domain.dto.CouponDiscountDTO;
import com.xc.promotion.domain.dto.OrderCouponDTO;
import com.xc.promotion.domain.dto.OrderProductDTO;

import java.util.List;

public interface IDiscountService {
    List<CouponDiscountDTO> findDiscountSolution(List<OrderProductDTO> orderProducts);

    CouponDiscountDTO queryDiscountDetailByOrder(OrderCouponDTO orderCouponDTO);
}
