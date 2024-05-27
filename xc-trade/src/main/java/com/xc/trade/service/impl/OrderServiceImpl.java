package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xc.api.client.product.ProductClient;
import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.*;
import com.xc.trade.constants.RedisConstants;
import com.xc.trade.entity.Address;
import com.xc.trade.entity.Orders;
import com.xc.trade.entity.OrderDetails;
import com.xc.trade.entity.ShoppingChart;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.GoodsCategroyReportsVO;
import com.xc.trade.entity.vo.GoodsSpuReportsVO;
import com.xc.trade.entity.vo.OrderVO;
import com.xc.trade.mapper.AddressMapper;
import com.xc.trade.mapper.OrderDetailsMapper;
import com.xc.trade.mapper.OrderMapper;
import com.xc.trade.mapper.ShoppingChartMapper;
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
    OrderDetailsMapper orderDetailsMapper;

    @Resource
    AddressMapper addressMapper;

    @Resource
    PromotionClient promotionClient;

    @Resource
    ProductClient productClient;

    @Autowired
    StringRedisTemplate redisTemplate;


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

            redisTemplate.opsForValue()
                    .set(RedisConstants.SHOPPING_PREFIX+UserContext.getUser(),JsonUtils.parse(orderProducts).toString()
                            , Duration.ofMinutes(RedisConstants.DURATION_MINUTES));


            List<CouponDiscountDTO> discounts = promotionClient.findDiscountSolution(orderProducts)
                    .stream().map(obj->obj.setDiscountDetail(null)).collect(Collectors.toList());

            double sum = skuById.stream().map(sku->sku.getNum()*sku.getPrice())
                    .mapToDouble(Double::doubleValue).reduce(0.0, Double::sum);
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
                res.setFinalPrice(sum - maxAmount / 100);
                res.setDiscountAmount((double) (maxAmount / 100));
                res.setRules(rule);
                res.setShoppingCharts(shoppingCharts);

                String  parse = JsonUtils.parse(res).toString();
                redisTemplate.opsForValue().set(RedisConstants.ORDER_PREFIX+UserContext.getUser()
                        , parse, Duration.ofMinutes(RedisConstants.DURATION_MINUTES));
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


        String preView = redisTemplate.opsForValue().get(RedisConstants.ORDER_PREFIX + UserContext.getUser());

        if(!StringUtils.isEmpty(preView)){
            PreviewOrderDTO preViewDTO = JsonUtils.parseObj(preView)
                    .toBean(PreviewOrderDTO.class);
            if(preViewDTO.getShoppingCharts().equals(vo.getShoppingCharts())){
                orders.setCouponId(preViewDTO.getCoupons().stream()
                        .map(String::valueOf).collect(Collectors.joining(",")));
                orders.setFinalPrice((int) (preViewDTO.getFinalPrice()*100));
                orders.setRawPrice((int)(preViewDTO.getRawPrice()*100));
                orders.setUserId(UserContext.getUser());
                boolean save = save(orders);
                if(!save){
                    throw new CommonException("订单创建失败");
                }
                updateChartAndDetails(orders,vo);
                return true;
            }
        }


        return true;
    }

    public void updateChartAndDetails(Orders orders, OrderVO vo){
        List<ShoppingChart> shoppingList = chartMapper.selectList(new LambdaQueryWrapper<ShoppingChart>()
                .eq(ShoppingChart::getUserId, UserContext.getUser()).in(ShoppingChart::getId,vo.getShoppingCharts()));
        if(CollUtils.isEmpty(shoppingList)) {
            throw new CommonException("");
        }

        String skuList = redisTemplate.opsForValue().get(RedisConstants.SHOPPING_PREFIX + UserContext.getUser());
        CouponDiscountDTO couponDiscountDTO=null;
        if(!StringUtils.isEmpty(skuList)){
            List<OrderProductDTO> dtoList = JsonUtils.parseArray(skuList).toList(OrderProductDTO.class);
            List<Long> skuIds = dtoList.stream().map(OrderProductDTO::getId).collect(Collectors.toList());
            List<Long> skuFromChats = shoppingList.stream().map(ShoppingChart::getSkuId).collect(Collectors.toList());
            skuIds.retainAll(skuFromChats);
            List<OrderProductDTO> finalDto = dtoList.stream().filter(p -> skuIds.contains(p.getId())).collect(Collectors.toList());
            couponDiscountDTO = promotionClient
                    .queryDiscountDetailByOrder(new OrderCouponDTO(vo.getCoupons(), finalDto));
        }

        assert couponDiscountDTO != null;
        Map<Long, Integer> map = couponDiscountDTO.getDiscountDetail();

        shoppingList.parallelStream()
                .map(shoppingChart -> {
                    OrderDetails orderDetails = BeanUtils.copyBean(shoppingChart, OrderDetails.class);
                    orderDetails.setOrderId(orders.getId());
                    orderDetails.setFinalPrice(shoppingChart.getPrice()-map.get(shoppingChart.getSkuId()));
                    return orderDetails;
                })
                .forEach(orderDetailsMapper::insert);
        List<Long> shoppingIds = shoppingList
                .stream().map(ShoppingChart::getId).collect(Collectors.toList());
        if(shoppingIds.size() != chartMapper.deleteBatchIds(shoppingIds)){
            throw new CommonException("删除购物车失败");
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

}
