package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xc.api.client.product.ProductClient;
import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.dto.IdAndNumDTO;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.*;
import com.xc.trade.constants.RedisConstants;
import com.xc.trade.entity.po.Address;
import com.xc.trade.entity.po.Orders;
import com.xc.trade.entity.po.OrderDetails;
import com.xc.trade.entity.po.ShoppingChart;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.enums.OrdersStatus;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.OrderVO;
import com.xc.trade.mapper.AddressMapper;
import com.xc.trade.mapper.OrderDetailsMapper;
import com.xc.trade.mapper.OrderMapper;
import com.xc.trade.mapper.ShoppingChartMapper;
import com.xc.trade.service.IOrderDetailsService;
import com.xc.trade.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
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


    @Override
    public PreviewOrderDTO preViewFromChart(List<Long> shoppingCharts) {
        if(CollUtils.isEmpty(shoppingCharts)){
            throw new CommonException("需要shoppingChartId");
        }
        PreviewOrderDTO res=new PreviewOrderDTO();
        List<ShoppingChart> list = chartMapper.selectList(new LambdaQueryWrapper<ShoppingChart>()
                .eq(ShoppingChart::getUserId, UserContext.getUser()).in(ShoppingChart::getId,shoppingCharts));

        if(!CollUtils.isEmpty(list)){
            List<SkuPageVO> skuById = productClient.getSkuById(list.stream()
                    .map(ShoppingChart::getSkuId).collect(Collectors.toSet()));
            String chartsKey = shoppingCharts.stream().map(String::valueOf).collect(Collectors.joining(","));

            redisTemplate.opsForValue()
                    .set(RedisConstants.SHOPPING_PREFIX+UserContext.getUser()+":"+chartsKey,JsonUtils.parse(skuById).toString()
                            , Duration.ofMinutes(RedisConstants.DURATION_MINUTES));
            if(CollUtils.isEmpty(skuById)){
                throw new CommonException("无有效sku id");
            }
            List<OrderProductDTO> orderProducts = skuById.stream().map(obj -> {
                OrderProductDTO dto = new OrderProductDTO();
                dto.setId(obj.getId());
                dto.setPrice((int) (obj.getPrice() * 100)*obj.getNum());
                dto.setCateId(obj.getCategoryId());
                return dto;
            }).collect(Collectors.toList());


            List<CouponDiscountDTO> discounts = promotionClient.findDiscountSolution(orderProducts)
                    .stream().map(obj->obj.setDiscountDetail(null)).collect(Collectors.toList());

            Integer sum = skuById.stream().map(sku->sku.getNum()*sku.getPrice())
                    .mapToInt(Integer::intValue).reduce(0,Integer::sum);
            res.setFinalPrice(sum);
            res.setRawPrice(sum);
            if(!CollUtils.isEmpty(discounts)) {
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
        if(address==null){
            throw new CommonException("addressId invalid");
        }

        Orders orders = new Orders();
        orders.setAddressId(vo.getAddressId());
        orders.setUserId(UserContext.getUser());

        List<ShoppingChart> shoppingList = chartMapper.selectList(new LambdaQueryWrapper<ShoppingChart>()
                .eq(ShoppingChart::getUserId, UserContext.getUser()).in(ShoppingChart::getId,vo.getShoppingCharts()));
        if(CollUtils.isEmpty(shoppingList)) {
            throw new CommonException("");
        }

        String chartsKey = vo.getShoppingCharts().stream().map(String::valueOf).collect(Collectors.joining(","));

        String skuList = redisTemplate.opsForValue().get(RedisConstants.SHOPPING_PREFIX + UserContext.getUser()+":"+chartsKey);
        List<SkuPageVO> skuVOs=null;
        if(!StringUtils.isEmpty(skuList)){
            skuVOs = JsonUtils.parseArray(skuList).toList(SkuPageVO.class);
        }else {
            skuVOs=productClient.getSkuById(shoppingList.stream()
                    .map(ShoppingChart::getSkuId).collect(Collectors.toSet()));
        }

        List<OrderProductDTO> orderProducts = skuVOs.stream().map(obj -> {
            OrderProductDTO dto = new OrderProductDTO();
            dto.setId(obj.getId());
            dto.setPrice(obj.getPrice()*obj.getNum());
            dto.setCateId(obj.getCategoryId());
            return dto;
        }).collect(Collectors.toList());


        Integer reduce =  shoppingList.stream().map(chart -> chart.getQuantity() * chart.getPrice())
                .mapToInt(Integer::intValue).reduce(0, Integer::sum);
        orders.setRawPrice(reduce);
        orders.setFinalPrice(reduce);

        Map<Long, Integer> map=null;
        if(!CollUtils.isEmpty(vo.getCoupons())) {
            CouponDiscountDTO discountDTO = promotionClient.queryDiscountDetailByOrder(new OrderCouponDTO(vo.getCoupons(), orderProducts));
            if (discountDTO != null) {
                orders.setFinalPrice(reduce - discountDTO.getDiscountAmount());
                map = discountDTO.getDiscountDetail();
            }
        }

        if(!save(orders)){
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

        if(!orderDetailsService.saveBatch(details)){
            throw new BizIllegalException("orderDetails插入失败");
        }

        List<IdAndNumDTO> list=new LinkedList<>();
        details.forEach(p->{
            IdAndNumDTO idAndNumDTO = new IdAndNumDTO();
            idAndNumDTO.setId(p.getSkuId());
            idAndNumDTO.setNum(p.getQuantity());
            list.add(idAndNumDTO);
        });

        if(!CollUtils.isEmpty(vo.getCoupons())) {
            promotionClient.writeOffCoupon(vo.getCoupons());
        }
        shoppingChartMapper.deleteBatchIds(vo.getShoppingCharts());
        productClient.updateSkuNum(list);
        //todo: 定时任务查询支付状态

        return true;
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
        return orderMapper.deliveryOrder(orderId)==1;
    }

    @Override
    public boolean finishOrder(Long orderId) {
        return orderMapper.updateOrderStatusByUser(OrdersStatus.SUCCESS.getValue(),orderId,UserContext.getUser())==1;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long orderId) {
        List<Long> detailsIDs = orderDetailsMapper.selectList(new LambdaQueryWrapper<OrderDetails>().eq(OrderDetails::getOrderId, orderId)).stream().map(OrderDetails::getId).collect(Collectors.toList());
        orderDetailsService.removeByIds(detailsIDs);
        return baseMapper.deleteById(orderId)==1;
    }
}
