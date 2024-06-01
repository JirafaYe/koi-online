package com.xc.trade.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCategroyReportsVO {

/**
     * 商品类型
     */
    @ExcelProperty(value = "商品大类", index = 0)
    private String category;

    /**
     * 销售额
     */
    @ExcelProperty(value = "销售量", index = 1)
    private Long count;

    /**
     * 销售金额
     */
    @ExcelProperty(value = "销售额", index = 2)
    private BigDecimal amount;


}
