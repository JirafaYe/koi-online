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

//    @Resource
//    AliPayConfig aliPayConfig;
//
//    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//    private static final String FORMAT ="JSON";
//    private static final String CHARSET ="utf-8";
//    private static final String SIGN_TYPE ="RSA2";

    /**
     * 支付接口
     * @param aliPay
     * @param httpResponse
     *
     */

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPay aliPay, HttpServletResponse httpResponse){
        aliPayService.pay(aliPay, httpResponse);
    }


    /**
     * 支付成功后的回调接口
     * @param request
     * @return
     * @throws Exception
     */
//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request) throws Exception {
//        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            System.out.println("=========支付宝异步回调========");
//
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//                 System.out.println(name + " = " + request.getParameter(name));
//            }
//
//            String tradeNo = params.get("out_trade_no");
//            String gmtPayment = params.get("gmt_payment");
//            String alipayTradeNo = params.get("trade_no");
////            System.out.println("==="+tradeNo+"==="+gmtPayment+"==="+alipayTradeNo);
//            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//                // 更新订单未已支付
////                ShopOrder order = new ShopOrder();
////                order.setId(tradeNo);
////                order.setStatus("1");
////                Date payTime = DateUtil.parse(gmtPayment, "yyyy-MM-dd HH:mm:ss");
////                order.setZhhifuTime(payTime);
////                shopOrderMapper.updateById(order);
//            }
//        }
//        return "success";
//    }
    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        return aliPayService.payNotify(request);
    }

    /**
     * 退款接口
     * @param refund
     * @return
     * @throws AlipayApiException
     */
//    @GetMapping("/refund")
//    public String returnPay(Refund refund) throws AlipayApiException {
//        // 7天无理由退款
//        String now = DateUtil.now();
////        Orders orders = ordersMapper.getByNo(aliPay.getTraceNo());
////        if (orders != null) {
////            // hutool工具类，判断时间间隔
////            long between = DateUtil.between(DateUtil.parseDateTime(orders.getPaymentTime()), DateUtil.parseDateTime(now), DateUnit.DAY);
////            if (between > 7) {
////                return "该订单已超过7天，不支持退款";
////            }
////        }
//
//
//        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
//                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
//                aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        // 2. 创建 Request，设置参数
//        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//        JSONObject bizContent = new JSONObject();
//        bizContent.set("trade_no", refund.getAlipayTraceNo());  // 支付宝回调的订单流水号
//        bizContent.set("refund_amount", refund.getTotalAmount());  // 订单的总金额
//        bizContent.set("out_trace_no", refund.getOutTraceNo());   //  商户订单编号
//
//        // 返回参数选项，按需传入
//        //JSONArray queryOptions = new JSONArray();
//        //queryOptions.add("refund_detail_item_list");
//        //bizContent.put("query_options", queryOptions);
//
//        request.setBizContent(bizContent.toString());
//
//        // 3. 执行请求
//        AlipayTradeRefundResponse response = alipayClient.execute(request);
//        if (response.isSuccess()) {  // 退款成功，isSuccess 为true
//            System.out.println("调用成功");
//
//            // 4. 更新数据库状态
////            ordersMapper.updatePayState(aliPay.getTraceNo(), "已退款", now);
//            return "退款成功";
//        } else {   // 退款失败，isSuccess 为false
//            System.out.println(response.getBody());
//            return response.getBody();
//        }
//
//    }
    @GetMapping("/refund")
    public String returnPay(Refund refund){
        return aliPayService.refund(refund);
    }
}