package com.xc.common.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;

public class AmountToTenThousandConverter implements Converter<BigDecimal> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Double.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public BigDecimal convertToJavaData(ReadCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public WriteCellData convertToExcelData(BigDecimal bigDecimal, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (bigDecimal != null) {
            // 将元转换为万元，并格式化保留两位小数
            BigDecimal amountInTenThousand = bigDecimal.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            return new WriteCellData<>(amountInTenThousand);
        } else {
            return new WriteCellData<>();
        }
    }


}