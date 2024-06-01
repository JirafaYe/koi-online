package com.xc.trade.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsSpuReportsVO  {
    /**
     * 商品类型
     */
    @ExcelProperty(value = "商品类型", index = 0)
    private String spuName;


    /**
     * 订单销售量
     */
    @ExcelProperty(value = "销售额", index = 1)
    private Long count;

    @ExcelProperty(value = "销售金额", index = 2)
    private BigDecimal amount;
    /**
     * 所属类型
     */
    @ExcelProperty(value = "商品大类", index = 3)
    private String category;
}
