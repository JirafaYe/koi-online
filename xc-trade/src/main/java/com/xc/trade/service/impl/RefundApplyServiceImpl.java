package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.constants.Constant;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.enums.UserType;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.DbException;
import com.xc.common.utils.AssertUtils;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.entity.dto.*;
import com.xc.trade.entity.enums.OrdersStatus;
import com.xc.trade.entity.enums.RefundClassifyStatus;
import com.xc.trade.entity.enums.RefundStatus;
import com.xc.trade.entity.po.OrderDetails;
import com.xc.trade.entity.po.Orders;
import com.xc.trade.entity.po.RefundApply;
import com.xc.trade.entity.query.RefundApplyPageQuery;
import com.xc.trade.entity.vo.RefundAndCouponVO;
import com.xc.trade.entity.vo.RefundApplyPageVO;
import com.xc.trade.entity.vo.RefundApplyVO;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.xc.trade.entity.enums.RefundStatus.AGREE;
import static com.xc.trade.entity.enums.RefundStatus.AGREE_RG;

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

    private final PromotionClient promotionClient;

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
        if(order.getStatus().equals(OrdersStatus.CLOSED.getValue()) || order.getPaymentId() == null){
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
                                AGREE :
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
        RefundResultDTO result = payService.refund(refundApplyDTO);

        handleRefundResult(result);
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

    @Override
    @Transactional
    public void approveRefundApply(ApproveFormDTO approveDTO) {
        RefundApply refundApply = getById(approveDTO.getId());
        if(refundApply == null){
            throw new BadRequestException("退款记录不存在");
        }
        RefundStatus status = refundApply.getStatus();

        if(!RefundStatus.UN_APPROVE.equals(status)
                && !RefundStatus.AGREE_RG.equals(status)
                && !RefundStatus.WAIT_M_RECEIVE.equals(status)){
            throw new BadRequestException("退款申请无法处理");
        }
        boolean agree = approveDTO.getApproveType() == 1;

        RefundApply r = new RefundApply();
        r.setId(refundApply.getId());
        r.setApprover(UserContext.getUser());
        r.setApproveTime(LocalDateTime.now());
        r.setApproveOpinion(approveDTO.getApproveOpinion());
        r.setRemark(approveDTO.getRemark());
        if(refundApply.getRefundClassify().equals(RefundClassifyStatus.RETURN_GOODS_REFUND)){
            if(status.equals(RefundStatus.UN_APPROVE)){
                r.setStatus(RefundStatus.AGREE_RG);
                r.setMessage(RefundStatus.desc(r.getStatus().getValue()));
            } else if(status.equals(RefundStatus.AGREE_RG)){
                r.setStatus(RefundStatus.WAIT_SENT_B);
                r.setMessage(RefundStatus.desc(r.getStatus().getValue()));
            }
        }

        if(!agree){
            r.setStatus(RefundStatus.REJECT);
            r.setMessage(RefundStatus.desc(r.getStatus().getValue()));
        }
        updateById(r);
        //仅退款 + 同意
        if(agree && (RefundClassifyStatus.REFUND_ONLY.equals(refundApply.getRefundClassify()) || status.equals(RefundStatus.WAIT_M_RECEIVE))){
            refundRequestAsync(refundApply);
        }
    }

    @Override
    public PageDTO<RefundApplyPageVO> RefundApplyPageQuery(RefundApplyPageQuery pageQuery) {
        Page<RefundApply> p = searchRefundApply(pageQuery);

        List<RefundApply> records = p.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(p);
        }

        Map<Long, UserInfoResVO> userMap = getRefundUserInfo(records);
        List<RefundApplyPageVO> list = new ArrayList<>(records.size());
        for (RefundApply r : records) {
            RefundApplyPageVO vo = BeanUtils.copyBean(r, RefundApplyPageVO.class);
            vo.setClassifyStatus(r.getRefundClassify().getValue());
            UserInfoResVO u = userMap.get(r.getCreater());
            vo.setProposerName(u.getAccount());

            vo.setApproverName(r.getApprover() == null ? "--" : userMap.get(r.getApprover()).getAccount());
            // 4.3.退款状态
            if(r.getRefundClassify().equals(RefundClassifyStatus.REFUND_ONLY)
                && r.getStatus().equals(AGREE)){
                vo.setRefundStatusDesc("同意退款");
            } else if (r.getRefundClassify().equals(RefundClassifyStatus.RETURN_GOODS_REFUND)
                && r.getStatus().equals(AGREE_RG)) {
                vo.setRefundStatusDesc("同意退货");
            } else {
                vo.setRefundStatusDesc(RefundStatus.desc(r.getStatus().getValue()));
            }
            if (RefundStatus.SUCCESS.equalsValue(r.getStatus().getValue())) {
                vo.setRefundSuccessTime(r.getFinishTime());
            }
            list.add(vo);
        }
        return PageDTO.of(p, list);
    }

    private Map<Long, UserInfoResVO> getRefundUserInfo(List<RefundApply> records) {
        Set<Long> userIds = new HashSet<>();
        for (RefundApply record : records) {
            userIds.add(record.getCreater());
            userIds.add(record.getApprover());
        }
        userIds.remove(null);
        List<UserInfoResVO> userInfos = userClient.getUserInfos(userIds);
        if(userInfos.size() != userIds.size()){
            throw new BizIllegalException("用户数据有误");
        }
        return userInfos.stream().collect(Collectors.toMap(UserInfoResVO::getUserId, u -> u));
    }

    private Page<RefundApply> searchRefundApply(RefundApplyPageQuery q) {
        Integer refundStatus = q.getRefundStatus();
        Integer classifyStatus = q.getRefundClassifyStatus();
        String defaultSortBy = "id";
        boolean isAsc = true;
        if (refundStatus != null) {
            if (RefundStatus.UN_APPROVE.equalsValue(refundStatus)) {
                defaultSortBy = Constant.DATA_FIELD_NAME_CREATE_TIME;
            } else {
                defaultSortBy = "approve_time";
                isAsc = false;
            }
        }
        Page<RefundApply> p = q.toMpPage(defaultSortBy, isAsc);

        return lambdaQuery()
                .eq(q.getId() != null, RefundApply::getId, q.getId())
                .eq(refundStatus != null, RefundApply::getStatus, refundStatus)
                .eq(classifyStatus != null , RefundApply::getRefundClassify, classifyStatus)
                .eq(q.getOrderDetailId() != null, RefundApply::getOrderDetailId, q.getOrderDetailId())
                .eq(q.getOrderId() != null, RefundApply::getOrderId, q.getOrderId())
                .ge(q.getApplyStartTime() != null, RefundApply::getCreateTime, q.getApplyStartTime())
                .le(q.getApplyEndTime() != null, RefundApply::getCreateTime, q.getApplyEndTime())
                .page(p);
    }

    @Override
    @Transactional
    public void cancelRefundApply(RefundCancelDTO cancelDTO) {
        Long applyId = cancelDTO.getId();
        Long detailId = cancelDTO.getOrderDetailId();
        List<RefundApply> list = lambdaQuery()
                .eq(applyId != null, RefundApply::getId, applyId)
                .eq(detailId != null, RefundApply::getOrderDetailId, detailId)
                .list();
        if(CollUtils.isEmpty(list)){
            return;
        }
        RefundApply apply = list.get(0);
        if(!RefundStatus.UN_APPROVE.equals(apply.getStatus()) && !RefundStatus.WAIT_SENT_B.equals(apply.getStatus())){
            throw new BizIllegalException("退款申请已经处理过了");
        }

        RefundApply r = new RefundApply();
        r.setId(apply.getId());
        r.setStatus(RefundStatus.CANCEL);
        r.setMessage(RefundStatus.CANCEL.getDesc());
        boolean success = updateById(r);
        if(!success){
            throw new DbException("数据更新失败");
        }

        detailsService.lambdaUpdate()
                .eq(OrderDetails::getId, r.getOrderDetailId())
                .set(OrderDetails::getRefundStatus, r.getStatus())
                .update();
    }

    @Override
    public void userDelivery(Long id) {
        RefundApply refundApply = getById(id);
        RefundClassifyStatus refundClassify = refundApply.getRefundClassify();
        if(refundClassify.equals(RefundClassifyStatus.REFUND_ONLY)){
            throw new BadRequestException("改订单为仅退款");
        }
        if(!refundApply.getStatus().equals(RefundStatus.WAIT_SENT_B)){
            throw new BadRequestException("改订单状态错误");
        }

        RefundApply r = new RefundApply();
        r.setId(refundApply.getId());
        r.setStatus(RefundStatus.WAIT_M_RECEIVE);
        updateById(r);
    }

    @Override
    public RefundApplyVO queryRefundDetailById(Long id) {
        RefundApply apply = getById(id);
        if(apply == null){
            throw new BadRequestException("退款记录不存在");
        }
        RefundApplyVO vo = BeanUtils.copyBean(apply, RefundApplyVO.class);
        vo.setRefundClassify(apply.getRefundClassify().getValue());
        Orders order = orderMapper.selectById(apply.getOrderId());
        if(order == null){
            throw new BadRequestException("订单不存在");
        }
        vo.setOrderTime(order.getCreateTime());
        vo.setPaySuccessTime(order.getPayTime());

        Set<Long> uIds = new HashSet<>();
        uIds.add(apply.getCreater());
        uIds.add(apply.getUserId());
        List<UserInfoResVO> userInfos = userClient.getUserInfos(uIds);
        AssertUtils.isNotEmpty(userInfos, "用户不存在");
        Map<Long, UserInfoResVO> userMap = userInfos.stream().collect(Collectors.toMap(UserInfoResVO::getUserId, u -> u));

        UserInfoResVO u = userMap.get(apply.getUserId());
        vo.setUserName(u.getAccount());
        vo.setRefundProposerName(userMap.get(apply.getCreater()).getAccount());

        OrderDetails detail = detailsService.getById(apply.getOrderDetailId());
        if(detail == null){
            throw new BadRequestException("订单不存在");
        }
        vo.setName(detail.getSpuName());
        vo.setAttributes(detail.getAttributes());
        vo.setPrice(detail.getPrice());
        vo.setRealPayAmount(detail.getFinalPrice());
        return vo;
    }

    @Override
    public RefundApplyVO queryRefundDetailByDetailId(Long detailId) {
        List<RefundApply> refundApplies = queryByDetailId(detailId);
        if(CollUtils.isEmpty(refundApplies)){
            return null;
        }
        RefundApply refundApply = refundApplies.get(0);
        RefundApplyVO vo = BeanUtils.copyBean(refundApply, RefundApplyVO.class);
        vo.setRefundClassify(refundApply.getRefundClassify().getValue());

        Orders order = orderMapper.selectById(refundApply.getOrderId());
        vo.setPaySuccessTime(order.getPayTime());
        return vo;
    }

    @Override
    public List<RefundApply> queryApplyToSend(int index, int size) {
        Page<RefundApply> page = lambdaQuery()
                .eq(RefundApply::getStatus, AGREE.getValue())
                .page(new Page<>(index, size));
        if (page == null || CollUtils.isEmpty(page.getRecords())) {
            return CollUtils.emptyList();
        }
        return page.getRecords();
    }

    @Override
    @Transactional
    public boolean checkRefundStatus(RefundApply r) {
        RefundStatus status = r.getStatus();
        return !status.equals(AGREE);
    }

    @Override
    public RefundAndCouponVO getRefundAndCoupon() {
        Integer countNum = lambdaQuery()
                .in(RefundApply::getStatus, RefundStatus.UN_APPROVE, RefundStatus.WAIT_M_RECEIVE)
                .count();
        Integer couponNum = promotionClient.getNum();
        if(countNum == null){
            countNum = 0;
        }
        if(couponNum == null){
            couponNum = 0;
        }
        return new RefundAndCouponVO(countNum, couponNum);
    }

    private List<RefundApply> queryByDetailId(Long id) {
        List<RefundApply> list = baseMapper.queryByDetailId(id);
        return CollUtils.isEmpty(list) ? CollUtils.emptyList() : list;
    }

    private void refundRequestAsync(RefundApply refundApply) {
        threadPoolTaskExecutor.execute(() -> this.sendRefundRequest(refundApply)) ;
    }
}
