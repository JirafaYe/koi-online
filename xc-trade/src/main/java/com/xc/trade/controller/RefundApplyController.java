package com.xc.trade.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.dto.ApproveFormDTO;
import com.xc.trade.entity.dto.RefundCancelDTO;
import com.xc.trade.entity.dto.RefundFormDTO;
import com.xc.trade.entity.query.RefundApplyPageQuery;
import com.xc.trade.entity.vo.RefundApplyPageVO;
import com.xc.trade.entity.vo.RefundApplyVO;
import com.xc.trade.service.IRefundApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * 退款申请
 *
 * @author Koi
 * @since 2024-05-28
 */
@RestController
@RequestMapping("/refund-apply")
@RequiredArgsConstructor
public class RefundApplyController {

    private final IRefundApplyService refundApplyService;

    /**
     * 退款申请
     */
    @PostMapping
    public void applyRefund(@Valid @RequestBody RefundFormDTO refundFormDTO){
        refundApplyService.applyRefund(refundFormDTO);
    }

    /**
     * 审批退款申请
     */
    @PostMapping("/approval")
    public void approveRefundApply(@Valid @RequestBody ApproveFormDTO approveDTO){
        refundApplyService.approveRefundApply(approveDTO);
    }


    /**
     * 分页查询退款申请
     */
    @GetMapping("/page")
    public PageDTO<RefundApplyPageVO> queryRefundApplyByPage(RefundApplyPageQuery pageQuery){
        return refundApplyService.RefundApplyPageQuery(pageQuery);
    }

    /**
     * 取消退款申请
     */
    @PutMapping("/cancel")
    public void cancelRefundApply(@Valid @RequestBody RefundCancelDTO cancelDTO){
        refundApplyService.cancelRefundApply(cancelDTO);
    }

    /**
     * 用户已寄出
     */
    @PutMapping("/{id}")
    public void userDelivery(@PathVariable("id") Long id){
        refundApplyService.userDelivery(id);
    }

    /**
     * 根据id查询退款详情
     */
    @GetMapping("/{id}")
    public RefundApplyVO queryRefundDetailById(@PathVariable("id") Long id){
        return refundApplyService.queryRefundDetailById(id);
    }

    /**
     * 根据子订单id查询退款详情
     */
    @GetMapping("/detail/{id}")
    public RefundApplyVO queryRefundDetailByDetailId(@PathVariable("id") Long detailId){
        return refundApplyService.queryRefundDetailByDetailId(detailId);
    }
}
