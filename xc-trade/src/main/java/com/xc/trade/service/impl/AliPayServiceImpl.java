package com.xc.trade.service.impl;

import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.common.utils.UserContext;
import com.xc.trade.config.AliPayConfig;
import com.xc.trade.entity.dto.RefundApplyDTO;
import com.xc.trade.entity.po.Payment;
import com.xc.trade.entity.dto.AliPay;
import com.xc.trade.entity.dto.Refund;
import com.xc.trade.entity.po.RefundApply;
import com.xc.trade.mapper.OrderMapper;
import com.xc.trade.mapper.PaymentMapper;
import com.xc.trade.service.IAliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;

import com.alipay.easysdk.factory.Factory;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AliPayServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IAliPayService {

    @Resource
    PaymentMapper paymentMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    AliPayConfig aliPayConfig;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

    @Override
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        //设置支付成功的页面跳转
//        request.setReturnUrl(aliPayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        try {
            httpResponse.getWriter().write(form);
            log.info("支付成功");
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();

            /**
             * 支付成功后将此支付信息写入数据库
             */
            Payment payment = new Payment();
            UserContext.setUser(1792188141227216896L);
            payment.setOrderId(Long.valueOf(aliPay.getTraceNo()));
//            payment.setUserId(UserContext.getUser());
            payment.setUserId(UserContext.getUser());
            payment.setTotalAmount(aliPay.getTotalAmount());
            paymentMapper.insert(payment);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String payNotify(HttpServletRequest request) {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            System.out.println("=========支付宝异步回调========");
            log.info("支付宝异步回调");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
//                System.out.println(name + " = " + request.getParameter(name));
            }

//            String tradeNo = params.get("out_trade_no");
//            String gmtPayment = params.get("gmt_payment");
//            String alipayTradeNo = params.get("trade_no");
//            System.out.println("==="+tradeNo+"==="+gmtPayment+"==="+alipayTradeNo);
            // 支付宝验签
            try {
                if (Factory.Payment.Common().verifyNotify(params)) {
                    log.info("支付宝验签通过");
//                    System.out.println("交易名称: " + params.get("subject"));
//                    System.out.println("交易状态: " + params.get("trade_status"));
//                    System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                    System.out.println("商户订单号: " + params.get("out_trade_no"));
//                    System.out.println("交易金额: " + params.get("total_amount"));
//                    System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                    System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                    System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                    log.info("交易名称: " + params.get("subject"));
                    log.info("交易状态: " + params.get("trade_status"));
                    log.info("支付宝交易凭证号: " + params.get("trade_no"));
                    log.info("商户订单号: " + params.get("out_trade_no"));
                    log.info("交易金额: " + params.get("total_amount"));
                    log.info("买家在支付宝唯一id: " + params.get("buyer_id"));
                    log.info("买家付款时间: " + params.get("gmt_payment"));
                    log.info("买家付款金额: " + params.get("buyer_pay_amount"));

                    //更新支付状态
                    LambdaQueryWrapper<Payment> lqw = new LambdaQueryWrapper<Payment>();
                    lqw.eq(Payment::getOrderId,Long.valueOf(params.get("out_trade_no")));
                    Payment payment = paymentMapper.selectOne(lqw);
                    payment.setPayStatus(1);
                    payment.setContent(String.valueOf(params.get("trade_status")));
                    payment.setPaymentId(String.valueOf(params.get("trade_no")));
                    paymentMapper.updateById(payment);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "success";
    }

    @Override
    public String refund(RefundApplyDTO refund) {

        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
                aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        // 2. 创建 Request，设置参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        Long orderId = refund.getOrderId();
        LambdaQueryWrapper<Payment> lqw = new LambdaQueryWrapper<Payment>();
        lqw.eq(Payment::getOrderId, orderId);
        Payment payment = paymentMapper.selectOne(lqw);

        double left = payment.getTotalAmount()-Double.valueOf(refund.getRefundAmount());
        if (left == 0){
            payment.setPayStatus(0);
        }
        paymentMapper.updateById(payment);
        Double refundMoney =  Double.valueOf(refund.getRefundAmount()/100);

//        bizContent.set("trade_no", refund.getAlipayTraceNo());  // 支付宝回调的订单流水号
        bizContent.set("trade_no", payment.getPaymentId());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", refundMoney);  // 订单的总金额
        bizContent.set("out_trace_no", orderId);   //  商户订单编号
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = new String(UUID.randomUUID().toString());
        bizContent.set("out_request_no", out_request_no);

        // 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);

        request.setBizContent(bizContent.toString());

        // 3. 执行请求
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {  // 退款成功，isSuccess 为true
                System.out.println("调用成功");

                // 4. 更新数据库状态
//            ordersMapper.updatePayState(aliPay.getTraceNo(), "已退款", now);
                return "退款成功";
            } else {   // 退款失败，isSuccess 为false
                System.out.println(response.getBody());
                return response.getBody();
            }
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        return "退款失败";
    }
}
