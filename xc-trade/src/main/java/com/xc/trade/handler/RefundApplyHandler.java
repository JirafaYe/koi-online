package com.xc.trade.handler;


import com.xc.trade.entity.po.RefundApply;
import com.xc.trade.service.IRefundApplyService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RefundApplyHandler {

    private final IRefundApplyService refundApplyService;

    @XxlJob("refundRequestJobHandler")
    public void handleRefundRequest(){
        // 1.获取分片信息，作为页码，每页最多查询 2条，避免退款申请过于频繁
        int index = XxlJobHelper.getShardIndex() + 1;
        int size = 2;
        // 2.分页查询审批通过的退款申请
        List<RefundApply> list = refundApplyService.queryApplyToSend(index, size);
        for (RefundApply r : list) {
            boolean refundFinished = refundApplyService.checkRefundStatus(r);
            if(refundFinished){
                continue;
            }
            // 3.2.发送退款申请
            refundApplyService.sendRefundRequest(r);
        }
    }

}
