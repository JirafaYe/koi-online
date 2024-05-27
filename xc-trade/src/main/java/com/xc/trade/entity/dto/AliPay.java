package com.xc.trade.entity.dto;
 
import lombok.Data;

/**
 * @Author 
 * @Date Created in  2024/1/13 15:26
 * @DESCRIPTION: alipay接口参数
 * @Version V1.0
 */
@Data
public class AliPay {
   //需要支付的订单编号
   private String traceNo;
   //商品金额
   private double totalAmount;
   //商品名称
   private String subject;
   //订单追踪号，商户自己生成，作为支付唯一凭证
   private String alipayTraceNo;
}