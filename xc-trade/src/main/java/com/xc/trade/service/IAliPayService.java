package com.xc.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.dto.RefundApplyDTO;
import com.xc.trade.entity.dto.RefundResultDTO;
import com.xc.trade.entity.po.Payment;

import com.xc.trade.entity.dto.AliPay;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface IAliPayService extends IService<Payment> {

    void pay(AliPay aliPay, HttpServletResponse httpResponse);

    String payNotify(HttpServletRequest request);

    RefundResultDTO refund(RefundApplyDTO refund);

}
