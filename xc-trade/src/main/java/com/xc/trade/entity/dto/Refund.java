package com.xc.trade.entity.dto;

import lombok.Data;

@Data
public class Refund {

    //订单追踪号，支付成功后生成，作为支付唯一凭证
    private String alipayTraceNo;

    //商户订单号
//    private String outTraceNo;
    private String merchantId;

    //商品金额
    private double totalAmount;

}
