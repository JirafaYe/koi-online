package com.xc.trade.controller;


import com.xc.trade.entity.dto.RefundFormDTO;
import com.xc.trade.service.IRefundApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
     * @param refundFormDTO
     */
    @PostMapping
    public void applyRefund(@Valid @RequestBody RefundFormDTO refundFormDTO){
        refundApplyService.applyRefund(refundFormDTO);
    }

}
