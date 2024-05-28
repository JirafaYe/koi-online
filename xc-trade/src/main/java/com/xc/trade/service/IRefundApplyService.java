package com.xc.trade.service;

import com.xc.trade.entity.dto.RefundFormDTO;
import com.xc.trade.entity.dto.RefundResultDTO;
import com.xc.trade.entity.po.RefundApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 退款申请 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-28
 */
public interface IRefundApplyService extends IService<RefundApply> {

    void applyRefund(RefundFormDTO refundFormDTO);

    void sendRefundRequest(RefundApply refundApply);

    void handleRefundResult(RefundResultDTO result);
}
