package com.xc.trade.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.xc.common.utils.ObjectImportUtil;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 报表
 */
@RestController()
@RequestMapping("/reports")
public class ReportsController {

    @Resource
    IOrderService orderService;



    /**
     * 每日订单销量报表
     *
     * @return
     */
    @PostMapping("/flowReports")
    public List<FlowReportsVO> flowReports() {
        return orderService.flowReports();
    }

    @PostMapping("/downLoadFlowReports")
    public void downLoadFlowReports(@RequestBody List<FlowReportsVO> vo, HttpServletRequest req, HttpServletResponse res) {
        InputStream inputStream = this.getClass().getResourceAsStream("/template/每日流量报表.xlsx");
        String fileName = "流量报表";
        ObjectImportUtil.exceptionDownload(res, fileName, vo, inputStream);
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



    /**
     * 商品大类销售报表
     *
     * @return
     */
    @GetMapping("/goodsCategroyReports")
    public List<GoodsCategroyReportsVO> marketingReports() {
        return orderService.marketingReports();
    }



}
