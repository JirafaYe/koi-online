package com.xc.trade.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FlowReportsVO {
    /**
     * 日期
     */
    @ExcelProperty(value = "日期", index = 0)
    private LocalDateTime time;

    /**
     * 销售数量
     */
    @ExcelProperty(value = "销售数量", index = 1)
    private Long flow;

    /**
     * 销售额
     */
    @ExcelProperty(value = "销售金额", index = 2)
    private BigDecimal amount;
}
