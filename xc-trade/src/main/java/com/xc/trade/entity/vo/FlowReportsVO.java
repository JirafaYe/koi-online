package com.xc.trade.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.converters.localdatetime.LocalDateNumberConverter;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeDateConverter;
import com.alibaba.excel.converters.longconverter.LongNumberConverter;
import com.xc.common.utils.AmountToTenThousandConverter;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ColumnWidth(15)     //行宽
@ContentStyle(dataFormat = 2)
@HeadFontStyle(fontHeightInPoints = 11)
public class FlowReportsVO {
    /**
     * 日期
     */
    @ExcelProperty(value = "日期", index = 0,converter = LocalDateTimeDateConverter.class)
    private LocalDateTime time;

    /**
     * 销售数量
     */
    @ExcelProperty(value = "销售数量", index = 1,converter = LongNumberConverter.class)
    private Long flow;

    /**
     * 销售额
     */
    @ExcelProperty(value = "销售金额(元)", index = 2,converter =  AmountToTenThousandConverter.class)
    private BigDecimal amount;
}
