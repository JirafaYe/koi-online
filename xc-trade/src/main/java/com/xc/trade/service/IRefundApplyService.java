package com.xc.trade.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.dto.ApproveFormDTO;
import com.xc.trade.entity.dto.RefundCancelDTO;
import com.xc.trade.entity.dto.RefundFormDTO;
import com.xc.trade.entity.dto.RefundResultDTO;
import com.xc.trade.entity.po.RefundApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.query.RefundApplyPageQuery;
import com.xc.trade.entity.vo.RefundAndCouponVO;
import com.xc.trade.entity.vo.RefundApplyPageVO;
import com.xc.trade.entity.vo.RefundApplyVO;

import java.util.List;

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

    void approveRefundApply(ApproveFormDTO approveDTO);

    PageDTO<RefundApplyPageVO> RefundApplyPageQuery(RefundApplyPageQuery pageQuery);

    void cancelRefundApply(RefundCancelDTO cancelDTO);

    void userDelivery(Long id);

    RefundApplyVO queryRefundDetailById(Long id);

    RefundApplyVO queryRefundDetailByDetailId(Long detailId);

    List<RefundApply> queryApplyToSend(int index, int size);

    boolean checkRefundStatus(RefundApply r);

    RefundAndCouponVO getRefundAndCoupon();
}
