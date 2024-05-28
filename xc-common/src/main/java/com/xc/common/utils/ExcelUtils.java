package com.xc.common.utils;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



/**
 * Excel工具类
 *
 * @author liqin
 * @date 2020/10/9 15:14
 */
@Slf4j
public class ExcelUtils {

    private ExcelUtils() {
    }

    /**
     * 设置响应流和文件名称
     * <p>
     * desciption
     *
     * @param res      响应流
     * @param fileName 文件名
     * @return void
     * @Date 2023/12/5 16:04
     */
    public static void setResponse(HttpServletResponse res, String fileName) {
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setCharacterEncoding("utf-8");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 设置Content-Disposition头部信息
        res.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }

}
