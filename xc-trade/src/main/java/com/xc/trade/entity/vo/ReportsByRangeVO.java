package com.xc.trade.entity.vo;

import com.alipay.api.internal.mapping.ApiField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportsByRangeVO {


    /**
     * 待发货数量
     */
    private Integer waitGoodsCount;
    /**
     * 退款待处理数量
     */
    private Integer waitRefundProcessCount;
    /**
     * 成交额
     */
    private BigDecimal tradedAmount;
    /**
     * 成交数量
     */
    private Integer tradedCount;
    /**
     * 待支付数量
     */
    private Integer payingCount;

}
