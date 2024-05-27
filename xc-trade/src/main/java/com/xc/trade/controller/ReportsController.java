package com.xc.trade.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.service.IOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 报表
 */
@RestController()
@RequestMapping("/reports")
public class ReportsController {

    @Resource
    IOrderService orderService;

    private static final String FILE_PATH = "f:/project/";
    private static final String FILE_NAME = "流量报表.xlsx";

    /**
     * 每日订单销量报表
     *
     * @return
     */
    @GetMapping("/flowReports")
    public List<FlowReportsVO> flowReports() {
        return orderService.flowReports();
    }
    @GetMapping("/downLoadFlowReports")
    public void downLoadFlowReports(@RequestBody List<FlowReportsVO> vo, HttpServletResponse response){
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // 设置内容类型为Excel
        response.setHeader("Content-Disposition", "attachment; filename=\"流量报表.xlsx\""); // 触发文件下载，并指定文件名
//        FileOutputStream out = new FileOutputStream(FILE_PATH + FILE_NAME);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) { // 使用try-with-resources自动管理资源
            ExcelWriter writer = EasyExcelFactory.getWriter(out);

            // 写仅有一个 Sheet 的 Excel 文件
            Sheet sheet1 = new Sheet(1, 0, FlowReportsVO.class);
            sheet1.setSheetName("流量报表");

            // 写数据到 Writer 上下文中
            writer.write(vo, sheet1);

            // 将输出流转换为字节数组
            byte[] bytes = out.toByteArray();
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            // 处理可能发生的IO异常
            e.printStackTrace();
        }

    }


    /**
     * 商品小类销售报表
     *
     * @return
     */
    @GetMapping("/goodsSpuReports")
    public List<GoodsSpuReportsVO> achieveReports() {
        return orderService.achieveReports();
    }
    @GetMapping("/downLoadSpuReports")
    public void downLoadSpuReports(@RequestBody List<GoodsSpuReportsVO> vo, HttpServletResponse response){

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // 设置内容类型为Excel
        response.setHeader("Content-Disposition", "attachment; filename=\"商品报表.xlsx\""); // 触发文件下载，并指定文件名
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) { // 使用try-with-resources自动管理资源
            ExcelWriter writer = EasyExcelFactory.getWriter(out);
            // 写仅有一个 Sheet 的 Excel 文件
            Sheet sheet1 = new Sheet(1, 0, GoodsSpuReportsVO.class);
            sheet1.setSheetName("商品报表");

            // 写数据到 Writer 上下文中
            writer.write(vo, sheet1);

            // 将输出流转换为字节数组
            byte[] bytes = out.toByteArray();
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            // 处理可能发生的IO异常
            e.printStackTrace();
        }

    }

    /**
     * 商品大类销售报表
     *
     * @return
     */
    @GetMapping("/goodsCategroyReports")
    public List<GoodsCategroyReportsVO> marketingReports() {
        return orderService.marketingReports();
    }

    @GetMapping("/downLoadCategroyReports")
    public void downLoadCategroyReports(@RequestBody List<GoodsCategroyReportsVO> vo, HttpServletResponse response){

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // 设置内容类型为Excel
        response.setHeader("Content-Disposition", "attachment; filename=\"商品报表.xlsx\""); // 触发文件下载，并指定文件名
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) { // 使用try-with-resources自动管理资源
            ExcelWriter writer = EasyExcelFactory.getWriter(out);
            // 写仅有一个 Sheet 的 Excel 文件
            Sheet sheet1 = new Sheet(1, 0, GoodsCategroyReportsVO.class);
            sheet1.setSheetName("商品报表");

            // 写数据到 Writer 上下文中
            writer.write(vo, sheet1);

            // 将输出流转换为字节数组
            byte[] bytes = out.toByteArray();
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            // 处理可能发生的IO异常
            e.printStackTrace();
        }

    }

}
