package com.xc.trade.controller;

import cn.hutool.core.date.DateUtil;
//import com.alibaba.fastjson.JSONObject;
import cn.hutool.json.JSONObject;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.factory.Factory;

import com.xc.trade.config.AliPayConfig;
import com.xc.trade.entity.dto.AliPay;
import com.xc.trade.entity.dto.Refund;
import com.xc.trade.service.IAliPayService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author
 * @Date Created in  2023/5/5 15:23
 * @DESCRIPTION:
 * @Version V1.0
 */
@RestController
@RequestMapping("alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {

    @Resource
    IAliPayService aliPayService;


    /**
     * 支付接口
     * @param aliPay
     * @param httpResponse
     */
    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPay aliPay, HttpServletResponse httpResponse){
        aliPayService.pay(aliPay, httpResponse);
    }


    /**
     * 支付成功后的回调接口
     * @param request
     * @return
     */
    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request){
        return aliPayService.payNotify(request);
    }

    /**
     * 退款接口
     * @param refund
     * @return
     */
    @GetMapping("/refund")
    public String returnPay(Refund refund){
        return aliPayService.refund(refund);
    }
}