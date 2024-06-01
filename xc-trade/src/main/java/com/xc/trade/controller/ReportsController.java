package com.xc.trade.controller;

;
import com.xc.trade.entity.dto.ReportsByRangeReqDTO;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.ReportsByRangeVO;
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

    /**
     * 下载每日订单销量报表
     * @param req
     * @param res
     */
    @PostMapping("/downLoadFlowReports")
    public void downLoadFlowReports(HttpServletRequest req, HttpServletResponse res) {

        InputStream inputStream = this.getClass().getResourceAsStream("/template/每日流量报表.xlsx");
        String fileName = "流量报表";
        orderService.downLoadFlowReports(res);
//        ObjectImportUtil.exceptionDownload(res, fileName, vo, inputStream);
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

    /**
     * 根据时间范围查询报表
     *
     * @param reportsByRangeVO
     * @return
     */
    @PostMapping("/getReportsByRange")
    public ReportsByRangeVO getReportsByRange(@RequestBody ReportsByRangeReqDTO reportsByRangeVO) {
        return orderService.getReportsByRange(reportsByRangeVO);
    }


}
