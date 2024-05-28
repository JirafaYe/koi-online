package com.xc.trade.service.impl;

import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.enums.UserType;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.DbException;
import com.xc.common.utils.AssertUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.config.ThreadPoolConfig;
import com.xc.trade.entity.dto.RefundApplyDTO;
import com.xc.trade.entity.dto.RefundFormDTO;
import com.xc.trade.entity.dto.RefundResultDTO;
import com.xc.trade.entity.enums.OrdersStatus;
import com.xc.trade.entity.enums.RefundClassifyStatus;
import com.xc.trade.entity.enums.RefundStatus;
import com.xc.trade.entity.po.OrderDetails;
import com.xc.trade.entity.po.Orders;
import com.xc.trade.entity.po.RefundApply;
import com.xc.trade.mapper.OrderMapper;
import com.xc.trade.mapper.RefundApplyMapper;
import com.xc.trade.service.IAliPayService;
import com.xc.trade.service.IOrderDetailsService;
import com.xc.trade.service.IRefundApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 退款申请 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-28
 */
@Service
@RequiredArgsConstructor
public class RefundApplyServiceImpl extends ServiceImpl<RefundApplyMapper, RefundApply> implements IRefundApplyService {

    private final IOrderDetailsService detailsService;

    private final OrderMapper orderMapper;

    private final UserClient userClient;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final IAliPayService payService;

    @Override
    @Transactional
    public void applyRefund(RefundFormDTO refundFormDTO) {
        Long userId = UserContext.getUser();
        OrderDetails detail = detailsService.getBaseMapper().selectById(refundFormDTO.getOrderDetailId());
        if(detail == null){
            throw new BadRequestException("订单不存在");
        }
        Orders order = orderMapper.selectById(detail.getOrderId());
        if(order == null){
            throw new BadRequestException("订单不存在");
        }
        if(!(order.getStatus().equals(OrdersStatus.CLOSED.getValue()) || order.getPaymentId() == null)){
            throw new BadRequestException("订单未支付或已关闭");
        }
        UserInfoResVO userInfo = userClient.getUserInfos(List.of(userId)).get(0);
        AssertUtils.isNotNull(userInfo, "用户不存在");
        boolean isUser = UserType.USER.equalsValue(userInfo.getUserRole());

        RefundApply refundApply = new RefundApply();
        refundApply.setOrderDetailId(detail.getId());
        refundApply.setOrderId(order.getId());
        refundApply.setUserId(order.getUserId());
        refundApply.setRefundAmount(detail.getFinalPrice());
        refundApply.setRefundReason(refundFormDTO.getRefundReason());
        refundApply.setQuestionDesc(refundFormDTO.getQuestionDesc());
        refundApply.setRefundClassify(refundFormDTO.getRefundClassify());
        refundApply.setMessage(isUser ? "用户申请" : "管理员申请");
        refundApply.setStatus(
                isUser ? RefundStatus.UN_APPROVE :
                        refundFormDTO.getRefundClassify().equals(RefundClassifyStatus.REFUND_ONLY) ?
                                RefundStatus.AGREE :
                                RefundStatus.AGREE_RG
        );
        boolean success = save(refundApply);
        if(!success){
            throw new DbException("退款数据新增失败");
        }
        orderMapper.updateOrderStatusByUser(OrdersStatus.REFUNDING.getValue(), refundApply.getOrderId(), userId);

        OrderDetails od = new OrderDetails();
        od.setId(detail.getId());
        od.setRefundStatus(refundApply.getStatus().getValue());
        detailsService.updateById(od);
        // 如果是管理员且为仅退款，发送异步退款请求，
        if(!isUser && refundFormDTO.getRefundClassify().equals(RefundClassifyStatus.REFUND_ONLY)){
            refundRequestAsync(refundApply);
        }
    }

    @Override
    @Transactional
    public void sendRefundRequest(RefundApply refundApply) {
        RefundApplyDTO refundApplyDTO = RefundApplyDTO
                .builder()
                .refundAmount(refundApply.getRefundAmount())
                .orderDetailId(refundApply.getOrderDetailId())
                .orderId(refundApply.getOrderId())
                .build();
//        RefundResultDTO result = payService.refund(refundApplyDTO);
//
//        handleRefundResult(result);
    }

    @Override
    @Transactional
    public void handleRefundResult(RefundResultDTO result) {
        RefundApply refundApply = getById(result.getRefundOrderId());
        if(refundApply == null){
            return;
        }

        RefundApply r = new RefundApply();
        r.setId(refundApply.getId());
        r.setRefundOrderNo(result.getRefundOrderNo());

        int status = result.getStatus();
        if(status == RefundResultDTO.RUNNING){
            updateById(r);
        }

        if(status == RefundResultDTO.SUCCESS){
            r.setStatus(RefundStatus.SUCCESS);
            r.setMessage(RefundStatus.SUCCESS.getDesc());
        }else {
            r.setStatus(RefundStatus.FAILED);
            r.setMessage(RefundStatus.FAILED.getDesc());
            r.setFailedReason(result.getMsg());
        }

        r.setFinishTime(LocalDateTime.now());
        updateById(r);

        detailsService
                .lambdaUpdate()
                .eq(OrderDetails::getId, refundApply.getOrderDetailId())
                .set(OrderDetails::getRefundStatus, r.getStatus())
                .update();
    }

    private void refundRequestAsync(RefundApply refundApply) {
        threadPoolTaskExecutor.execute(() -> this.sendRefundRequest(refundApply)) ;
    }
}
