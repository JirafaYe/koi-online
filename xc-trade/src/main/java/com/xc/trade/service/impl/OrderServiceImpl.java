package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.product.ProductClient;
import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.dto.IdAndNumDTO;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.*;
import com.xc.trade.constants.RedisConstants;
import com.xc.trade.entity.dto.OrderDTO;
import com.xc.trade.entity.dto.OrderDetailsDTO;
import com.xc.trade.entity.dto.ReportsByRangeReqDTO;
import com.xc.trade.entity.enums.RefundStatus;
import com.xc.trade.entity.po.*;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.enums.OrdersStatus;
import com.xc.trade.entity.query.OrderQuery;
import com.xc.trade.entity.vo.*;
import com.xc.trade.mapper.*;
import com.xc.trade.service.IOrderDetailsService;
import com.xc.trade.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements IOrderService {

    @Resource
    ShoppingChartMapper chartMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    IOrderDetailsService orderDetailsService;

    @Resource
    AddressMapper addressMapper;

    @Resource
    PromotionClient promotionClient;

    @Resource
    ProductClient productClient;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private ShoppingChartMapper shoppingChartMapper;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private RefundApplyMapper refundApplyMapper;


    @Override
    public PreviewOrderDTO preViewFromChart(List<Long> shoppingCharts) {
        if (CollUtils.isEmpty(shoppingCharts)) {
            throw new CommonException("需要shoppingChartId");
        }
        PreviewOrderDTO res = new PreviewOrderDTO();
        List<ShoppingChart> list = chartMapper.selectList(new LambdaQueryWrapper<ShoppingChart>()
                .eq(ShoppingChart::getUserId, UserContext.getUser()).in(ShoppingChart::getId, shoppingCharts));

        if (!CollUtils.isEmpty(list)) {
            List<SkuPageVO> skuById = productClient.getSkuById(list.stream()
                    .map(ShoppingChart::getSkuId).collect(Collectors.toSet()));

            validateSkuPageVO(skuById);

            String chartsKey = shoppingCharts.stream().map(String::valueOf).collect(Collectors.joining(","));

            redisTemplate.opsForValue()
                    .set(RedisConstants.SHOPPING_PREFIX + UserContext.getUser() + ":" + chartsKey, JsonUtils.parse(skuById).toString()
                            , Duration.ofMinutes(RedisConstants.DURATION_MINUTES));
            if (CollUtils.isEmpty(skuById)) {
                throw new CommonException("无有效sku id");
            }
            List<OrderProductDTO> orderProducts = skuById.stream().map(obj -> {
                OrderProductDTO dto = new OrderProductDTO();
                dto.setId(obj.getId());
                dto.setPrice((int) (obj.getPrice() * 100) * obj.getNum());
                dto.setCateId(obj.getCategoryId());
                return dto;
            }).collect(Collectors.toList());


            List<CouponDiscountDTO> discounts = promotionClient.findDiscountSolution(orderProducts)
                    .stream().map(obj -> obj.setDiscountDetail(null)).collect(Collectors.toList());

            Integer sum = skuById.stream().map(sku -> sku.getNum() * sku.getPrice())
                    .mapToInt(Integer::intValue).reduce(0, Integer::sum);
            res.setFinalPrice(sum);
            res.setRawPrice(sum);
            if (!CollUtils.isEmpty(discounts)) {
                int maxAmount = Integer.MIN_VALUE;
                List<String> rule = null;
                for (CouponDiscountDTO discount : discounts) {
                    if (discount.getDiscountAmount() > maxAmount) {
                        maxAmount = discount.getDiscountAmount();
                        rule = discount.getRules();
                        res.setCoupons(discount.getIds());
                    }
                }
                discounts.forEach(obj -> obj.setDiscountAmount(null));

                res.setDiscounts(discounts);
                res.setFinalPrice(sum - maxAmount);
                res.setDiscountAmount(maxAmount);
                res.setRules(rule);
                res.setShoppingCharts(shoppingCharts);
            }
        }
        return res;
    }

    @Override
    @Transactional
    public boolean createOrder(OrderVO vo) {
        Address address = addressMapper.selectById(vo.getAddressId());
        if (address == null) {
            throw new CommonException("addressId invalid");
        }

        address.setId(null);
        Orders orders = new Orders();
        orders.setAddress(JsonUtils.parse(address).toString());
        orders.setUserId(UserContext.getUser());

        List<ShoppingChart> shoppingList = chartMapper.selectList(new LambdaQueryWrapper<ShoppingChart>()
                .eq(ShoppingChart::getUserId, UserContext.getUser()).in(ShoppingChart::getId, vo.getShoppingCharts()));
        if (CollUtils.isEmpty(shoppingList)) {
            throw new CommonException("");
        }

        String chartsKey = vo.getShoppingCharts().stream().map(String::valueOf).collect(Collectors.joining(","));

        String skuList = redisTemplate.opsForValue().get(RedisConstants.SHOPPING_PREFIX + UserContext.getUser() + ":" + chartsKey);
        List<SkuPageVO> skuVOs;
        if (!StringUtils.isEmpty(skuList)) {
            skuVOs = JsonUtils.parseArray(skuList).toList(SkuPageVO.class);
        } else {
            skuVOs = productClient.getSkuById(shoppingList.stream()
                    .map(ShoppingChart::getSkuId).collect(Collectors.toSet()));
        }

        validateSkuPageVO(skuVOs);

        List<OrderProductDTO> orderProducts = skuVOs.stream().map(obj -> {
            OrderProductDTO dto = new OrderProductDTO();
            dto.setId(obj.getId());
            dto.setPrice(obj.getPrice() * obj.getNum());
            dto.setCateId(obj.getCategoryId());
            return dto;
        }).collect(Collectors.toList());


        Integer reduce = shoppingList.stream().map(chart -> chart.getQuantity() * chart.getPrice())
                .mapToInt(Integer::intValue).reduce(0, Integer::sum);
        orders.setRawPrice(reduce);
        orders.setFinalPrice(reduce);

        Map<Long, Integer> map = null;
        if (!CollUtils.isEmpty(vo.getCoupons())) {
            CouponDiscountDTO discountDTO = promotionClient.queryDiscountDetailByOrder(new OrderCouponDTO(vo.getCoupons(), orderProducts));
            if (discountDTO != null) {
                orders.setFinalPrice(reduce - discountDTO.getDiscountAmount());
                map = discountDTO.getDiscountDetail();
            }
        }

        if (!save(orders)) {
            throw new CommonException("订单创建失败");
        }


        Map<Long, Integer> finalMap = map;
        List<OrderDetails> details = skuVOs.stream().parallel().map(obj -> {
            OrderDetails orderDetails = BeanUtils.copyBean(obj, OrderDetails.class);
            orderDetails.setId(null);
            orderDetails.setOrderId(orders.getId());
            orderDetails.setSkuId(obj.getId());
            orderDetails.setQuantity(obj.getNum());
            orderDetails.setFinalPrice(obj.getPrice());
            if (finalMap != null) {
                orderDetails.setFinalPrice(obj.getPrice() - finalMap.get(obj.getId()));
            }
            return orderDetails;
        }).collect(Collectors.toList());

        if (!orderDetailsService.saveBatch(details)) {
            throw new BizIllegalException("orderDetails插入失败");
        }

        List<IdAndNumDTO> list = new LinkedList<>();
        details.forEach(p -> {
            IdAndNumDTO idAndNumDTO = new IdAndNumDTO();
            idAndNumDTO.setId(p.getSkuId());
            idAndNumDTO.setNum(-p.getQuantity());
            list.add(idAndNumDTO);
        });

        if (!CollUtils.isEmpty(vo.getCoupons())) {
            promotionClient.writeOffCoupon(vo.getCoupons());
        }
        shoppingChartMapper.deleteBatchIds(vo.getShoppingCharts());
        productClient.updateSkuNum(list);
        //todo: 定时任务查询支付状态

        return true;
    }

    public void validateSkuPageVO(List<SkuPageVO> skuVOs) {
        if (CollUtils.isEmpty(skuVOs)) {
            throw new BizIllegalException("skuId 无效");
        }
        Set<Long> invalidSku = skuVOs.stream()
                .filter(p -> !p.getAvailable().equals(true)).map(SkuPageVO::getId).collect(Collectors.toSet());
        invalidSku.addAll(skuVOs.stream()
                .filter(p -> p.getNum() <= 0).map(SkuPageVO::getId).collect(Collectors.toSet()));
        if (!CollUtils.isEmpty(invalidSku)) {
            throw new BizIllegalException("商品不合法，请重新添加:" + invalidSku);
        }
    }

    @Override
    public List<FlowReportsVO> flowReports() {
        return orderMapper.getflowReports();
    }

    @Override
    public List<GoodsSpuReportsVO> achieveReports() {
        return orderMapper.getAchieveReports();
    }

    @Override
    public List<GoodsCategroyReportsVO> marketingReports() {
        return orderMapper.getMarketingReports();
    }

    @Override
    public boolean delivery(Long orderId) {
        return orderMapper.deliveryOrder(orderId) == 1;
    }

    @Override
    public boolean finishOrder(Long orderId) {
        return orderMapper.updateOrderStatusByUser(OrdersStatus.SUCCESS.getValue(), orderId, UserContext.getUser()) == 1;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long orderId) {
        List<Long> detailsIDs = orderDetailsMapper.selectList(new LambdaQueryWrapper<OrderDetails>().eq(OrderDetails::getOrderId, orderId)).stream().map(OrderDetails::getId).collect(Collectors.toList());
        orderDetailsService.removeByIds(detailsIDs);
        return baseMapper.delete(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getId, orderId)
                .eq(Orders::getStatus, OrdersStatus.CLOSED.getValue())
                .or().eq(Orders::getStatus, OrdersStatus.SUCCESS.getValue())) == 1;
    }

    @Override
    @Transactional
    public boolean canceledOrder(Long orderId) {
        Orders one = lambdaQuery().eq(Orders::getId, orderId).eq(Orders::getDeliveryStatus, 0).one();
        if (one == null) {
            throw new BizIllegalException("该订单已发出不可取消");
        }
        List<OrderDetails> orderDetails = orderDetailsMapper.selectList(new LambdaQueryWrapper<OrderDetails>().eq(OrderDetails::getOrderId, orderId).isNull(OrderDetails::getRefundStatus));

        //todo: 判断是否支付and调用退款接口进行退款(reduce)
        Integer reduce = orderDetails.stream().map(p -> p.getQuantity() * p.getFinalPrice())
                .mapToInt(Integer::intValue).reduce(0, Integer::sum);

        orderDetailsService.updateBatchById(orderDetails.stream().map(p -> p.setCanceled(true)).collect(Collectors.toList()));
        orderMapper.updateOrderStatusByUser(OrdersStatus.CLOSED.getValue(), orderId, UserContext.getUser());

        List<IdAndNumDTO> list = new LinkedList<>();
        orderDetails.forEach(p -> {
            IdAndNumDTO idAndNumDTO = new IdAndNumDTO();
            idAndNumDTO.setId(p.getSkuId());
            idAndNumDTO.setNum(-p.getQuantity());
            list.add(idAndNumDTO);
        });

        productClient.updateSkuNum(list);

        return true;
    }

    @Override
    public PageDTO<OrderDTO> pageQuery(OrderQuery query) {
        List<Long> orders;
        LambdaQueryChainWrapper<Orders> wrapper = lambdaQuery().eq(Orders::getUserId, UserContext.getUser());
        if (!StringUtils.isEmpty(query.getSpuName())) {
            orders = orderDetailsMapper.selectOrdersBySpuName(query.getSpuName());
            wrapper.in(Orders::getId, orders);
        }
        Page<Orders> page = wrapper.page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<Orders> records = page.getRecords();
        PageDTO<OrderDTO> res = PageDTO.empty(page);
        if (!CollUtils.isEmpty(records)) {
            List<Long> idList = records.stream().map(Orders::getId).collect(Collectors.toList());
            List<OrderDetails> orderDetails = orderDetailsMapper.selectList(new LambdaQueryWrapper<OrderDetails>()
                    .in(OrderDetails::getOrderId, idList));
            HashMap<Long, List<OrderDetailsDTO>> dtoMap = new HashMap<>();
            orderDetails.forEach(p -> {
                if (!dtoMap.containsKey(p.getOrderId())) {
                    dtoMap.put(p.getOrderId(), List.of(BeanUtils.copyBean(p, OrderDetailsDTO.class)));
                } else {
                    List<OrderDetailsDTO> dtos = dtoMap.get(p.getOrderId());
                    dtos.add(BeanUtils.copyBean(p, OrderDetailsDTO.class));
                    dtoMap.put(p.getOrderId(), dtos);
                }
            });

            List<OrderDTO> collect = records.stream().map(p -> {
                OrderDTO orderDTO = BeanUtils.copyBean(p, OrderDTO.class);
                orderDTO.setDetails(dtoMap.get(orderDTO.getId()));
                orderDTO.setAddressVO(JsonUtils.parse(p.getAddress()).toBean(AddressVO.class));
                return orderDTO;
            }).collect(Collectors.toList());

            res = PageDTO.of(page, collect);
        }
        return res;
    }

    @Override
    public ReportsByRangeVO getReportsByRange(ReportsByRangeReqDTO reportsByRangeVO) {
        LocalDate startTime = reportsByRangeVO.getStartTime();
        LocalDate endTime = reportsByRangeVO.getEndTime();
        if (startTime == null) {
            startTime = LocalDate.now().minusDays(1);
        }
        if (endTime == null) {
            endTime = LocalDate.now();
        }
        ReportsByRangeVO ans = new ReportsByRangeVO();
        //待发货数量

        Integer count = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getDeliveryStatus, 0)
                .eq(Orders::getStatus, 1)
                .between(Orders::getCreateTime, startTime, endTime));
        ans.setWaitGoodsCount(count);
        //退款待处理数量
        Integer refundApplyCount = refundApplyMapper.selectCount(new LambdaQueryWrapper<RefundApply>()
                .eq(RefundApply::getStatus, RefundStatus.UN_APPROVE.getValue())
                .eq(RefundApply::getStatus, RefundStatus.WAIT_M_RECEIVE.getValue())
                .between(RefundApply::getCreateTime, startTime, endTime));
        ans.setWaitRefundProcessCount(refundApplyCount);
        //成交额（不考虑退款）
        List<Payment> payments = paymentMapper.selectList(new LambdaQueryWrapper<Payment>()
                .eq(Payment::getPayStatus, 1)
                .between(Payment::getCreateTime, startTime, endTime));
        double sum = payments.stream().map(Payment::getTotalAmount).mapToDouble(Double::doubleValue).sum();
        ans.setTradedAmount(BigDecimal.valueOf(sum));
        //成交数量
        int size = payments.size();
        ans.setTradedCount(size);
        //待支付数量
        Integer paying = paymentMapper.selectCount(new LambdaQueryWrapper<Payment>()
                .eq(Payment::getPayStatus, null)
                .between(Payment::getCreateTime, startTime, endTime));
        ans.setPayingCount(paying);
        return ans;
    }
}
