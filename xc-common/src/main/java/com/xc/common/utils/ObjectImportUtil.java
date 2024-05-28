package com.xc.common.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * 对象导入工具类
 * @author pyl
 */

@Slf4j
public class ObjectImportUtil {
    public static <T> void exceptionDownload(HttpServletResponse res, String fileName, List<T> Records,
                                        InputStream inputStream) {

        //设置响应流和返回文件名
        ExcelUtils.setResponse(res, fileName);
        ServletOutputStream outputStream = null;
        XSSFWorkbook excel = null;
        try {
            excel = new XSSFWorkbook(inputStream);
            outputStream = res.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取sheet页
        XSSFSheet sheet = excel.getSheetAt(0);
        //根据表头获取列数量
        XSSFRow headRow = sheet.getRow(0);
        short CellSum = headRow.getLastCellNum();
        for (int i = 0; i < Records.size(); i++) {
            // 创建新行
            sheet.createRow(i + 1);
            XSSFRow row = sheet.getRow(i + 1);
            for (int j = 0; j < CellSum; j++) {
                //创建列
                row.createCell(j);
            }
            //获取记录
            T record = Records.get(i);
            //获取对象所有字段
            for (Field field : record.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }
                //根据索引匹配表格索引依次赋值
                int index = annotation.index();
                try {
                    row.getCell(index).setCellValue((String) field.get(record));
                } catch (Exception e) {
                    log.error("index:{},record：{}，field：{}", index, record, field);
                }
            }
        }
        try {
            excel.write(outputStream);
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
