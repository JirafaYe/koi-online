package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xc.api.client.product.ProductClient;
import com.xc.api.client.promotion.PromotionClient;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.promotion.CouponDiscountDTO;
import com.xc.api.dto.promotion.OrderCouponDTO;
import com.xc.api.dto.promotion.OrderProductDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.entity.Order;
import com.xc.trade.entity.ShoppingChart;
import com.xc.trade.entity.dto.PreviewOrderDTO;
import com.xc.trade.entity.vo.FlowReportsVO;
import com.xc.trade.entity.vo.OrderVO;
import com.xc.trade.entity.vo.ShoppingChartVO;
import com.xc.trade.mapper.OrderMapper;
import com.xc.trade.mapper.ShoppingChartMapper;
import com.xc.trade.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    ShoppingChartMapper chartMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    PromotionClient promotionClient;

    @Resource
    ProductClient productClient;

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

            List<CouponDiscountDTO> discounts = promotionClient.findDiscountSolution(orderProducts)
                    .stream().map(obj->obj.setDiscountDetail(null)).collect(Collectors.toList());

            double sum = skuById.stream().map(sku->sku.getNum()*sku.getPrice())
                    .mapToDouble(Double::doubleValue).reduce(0.0, Double::sum);
            res.setFinal_price(sum);
            res.setRaw_price(sum);
            if(!CollUtils.isEmpty(discounts)){
                int maxAmount=Integer.MIN_VALUE;
                List<String> rule=null;
                for (CouponDiscountDTO discount : discounts) {
                    if(discount.getDiscountAmount()>maxAmount){
                        maxAmount=discount.getDiscountAmount();
                        rule=discount.getRules();
                    }
                }
                discounts.forEach(obj->obj.setDiscountAmount(null));

                res.setDiscounts(discounts);
                res.setFinal_price(sum-maxAmount/100);
                res.setDiscountAmount((double) (maxAmount/100));
                res.setRules(rule);
            }

        }
        return res;
    }

    @Override
    public List<FlowReportsVO> flowReports() {
        return orderMapper.getflowReports();
    }
}
