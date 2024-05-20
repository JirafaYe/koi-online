package com.xc.promotion.service.impl;

import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.promotion.domain.dto.CouponDiscountDTO;
import com.xc.promotion.domain.dto.OrderCouponDTO;
import com.xc.promotion.domain.dto.OrderProductDTO;
import com.xc.promotion.domain.enums.UserCouponStatus;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.CouponScope;
import com.xc.promotion.mapper.UserCouponMapper;
import com.xc.promotion.service.ICouponScopeService;
import com.xc.promotion.service.IDiscountService;
import com.xc.promotion.strategy.discount.Discount;
import com.xc.promotion.strategy.discount.DiscountStrategy;
import com.xc.promotion.utils.PermuteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DiscountServiceImpl implements IDiscountService {

    private  final UserCouponMapper userCouponMapper;

    private final ICouponScopeService scopeService;

    private final Executor discountSolutionExecutor;
    @Override
    public List<CouponDiscountDTO> findDiscountSolution(List<OrderProductDTO> orderProducts) {
        Long userId = UserContext.getUser();
        List<Coupon> coupons = userCouponMapper.queryMyCoupons(userId);
        if(CollUtils.isEmpty(coupons)){
            return CollUtils.emptyList();
        }
        //初筛
        int totalAmount = orderProducts.stream().mapToInt(OrderProductDTO::getPrice).sum();
        List<Coupon> availableCoupons = coupons.stream()
                .filter(c -> DiscountStrategy.getDiscount(c.getDiscountType()).canUse(totalAmount, c))
                .collect(Collectors.toList());
        if(CollUtils.isEmpty(availableCoupons)){
            return CollUtils.emptyList();
        }

        //找出可用的优惠券
        Map<Coupon, List<OrderProductDTO>> availableCouponMap = findAvailableCoupon(availableCoupons, orderProducts);
        if(CollUtils.isEmpty(availableCouponMap)){
            return CollUtils.emptyList();
        }
        //排列组合
        availableCoupons = new ArrayList<>(availableCouponMap.keySet());
        List<List<Coupon>> solutions = PermuteUtil.permute(availableCoupons);
        for (Coupon c : availableCoupons) {
            solutions.add(List.of(c));
        }
        //计算方案明细
        List<CouponDiscountDTO> list = Collections.synchronizedList(new ArrayList<>(solutions.size()));
        CountDownLatch latch = new CountDownLatch(solutions.size());
        for (List<Coupon> solution : solutions) {
            CompletableFuture.supplyAsync(() -> calculateSolutionDiscount(availableCouponMap, orderProducts, solution)
                            , discountSolutionExecutor
                    ).thenAccept(dto -> {
                        list.add(dto);
                        latch.countDown();;
                    });
        }
        //等待计算结束
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("优惠方案计算被中断，{}", e.getMessage());
        }
        // 筛选最优解
        return findBestSolution(list);
    }

    @Override
    public CouponDiscountDTO queryDiscountDetailByOrder(OrderCouponDTO orderCouponDTO) {
        List<Long> userCouponIds = orderCouponDTO.getUserCouponIds();
        List<Coupon> coupons = userCouponMapper.queryCouponByUserCouponIds(userCouponIds, UserCouponStatus.UNUSED);
        if(CollUtils.isEmpty(coupons)){
            return null;
        }
        Map<Coupon, List<OrderProductDTO>> availableCouponMap = findAvailableCoupon(coupons, orderCouponDTO.getProductList());
        if(CollUtils.isEmpty(availableCouponMap)){
            return null;
        }
        return calculateSolutionDiscount(availableCouponMap, orderCouponDTO.getProductList(), coupons);
    }

    private List<CouponDiscountDTO> findBestSolution(List<CouponDiscountDTO> list) {
        //记录最优解
        Map<String, CouponDiscountDTO> moreDiscount = new HashMap<>();
        Map<Integer, CouponDiscountDTO> lessCoupon = new HashMap<>();
        for (CouponDiscountDTO solution : list) {
            String ids = solution.getIds().stream()
                    .sorted(Long::compare)
                    .map(String::valueOf).collect(Collectors.joining(","));
            CouponDiscountDTO best = moreDiscount.get(ids);
            if(best != null && best.getDiscountAmount() >= solution.getDiscountAmount()){
                continue;
            }
            best = lessCoupon.get(solution.getDiscountAmount());
            int size = solution.getIds().size();
            if(size > 1 && best != null && best.getIds().size() <= size){
                continue;
            }
            //更新
            moreDiscount.put(ids, solution);
            lessCoupon.put(solution.getDiscountAmount(), solution);
        }
        //求交集
        Collection<CouponDiscountDTO> bestSolutions = CollUtils.intersection(moreDiscount.values(), lessCoupon.values());
        //按照优惠金额降序
        return bestSolutions.stream()
                .sorted(Comparator.comparingInt(CouponDiscountDTO::getDiscountAmount).reversed())
                .collect(Collectors.toList());
    }

    private CouponDiscountDTO calculateSolutionDiscount(Map<Coupon, List<OrderProductDTO>> couponMap, List<OrderProductDTO> products, List<Coupon> solution) {
        CouponDiscountDTO dto = new CouponDiscountDTO();
        Map<Long, Integer> detailMap = products.stream().collect(Collectors.toMap(OrderProductDTO::getId, c -> 0));
        dto.setDiscountDetail(detailMap);
        for (Coupon coupon : solution) {
            List<OrderProductDTO> availableProducts = couponMap.get(coupon);
            int totalAmount = availableProducts.stream()
                    .mapToInt(op -> op.getPrice() - detailMap.get(op.getId())).sum();
            Discount discount = DiscountStrategy.getDiscount(coupon.getDiscountType());
            if(!discount.canUse(totalAmount, coupon)){
                continue;
            }
            int discountAmount = discount.calculateDiscount(totalAmount, coupon);
            calculateDiscountDetails(detailMap, availableProducts, totalAmount, discountAmount);
            dto.getIds().add(coupon.getId());
            dto.getRules().add(discount.getRule(coupon));
            dto.setDiscountAmount(discountAmount + dto.getDiscountAmount());
        }
        return dto;
    }

    private void calculateDiscountDetails(Map<Long, Integer> detailMap, List<OrderProductDTO> products, int totalAmount, int discountAmount) {
        int times = 0;
        int remainDiscount = discountAmount;
        for (OrderProductDTO product : products) {
            times++;
            //计算折扣明细 课程价格在总价总的比列， 乘以总折扣
            int discount = 0;
            if(times == products.size()){
                discount = remainDiscount;
            } else{
                discount = discountAmount * product.getPrice() / totalAmount;
                remainDiscount -= discount;
            }
            //更新折扣明细
            detailMap.put(product.getId(), discount + detailMap.get(product.getId()));
        }
    }

    private Map<Coupon, List<OrderProductDTO>> findAvailableCoupon(List<Coupon> coupons, List<OrderProductDTO> products) {
        Map<Coupon, List<OrderProductDTO>> map = new HashMap<>(coupons.size());
        for (Coupon coupon : coupons) {
            List<OrderProductDTO> availableProducts = products;
            if(coupon.getSpecific()){
                List<CouponScope> scopes = scopeService.lambdaQuery().eq(CouponScope::getCouponId, coupon.getId()).list();
                List<Long> scopeIds = scopes.stream().map(CouponScope::getBizId).collect(Collectors.toList());
                availableProducts= products.stream().filter(c -> scopeIds.contains(c.getId())).collect(Collectors.toList());
            }
            if(CollUtils.isEmpty(availableProducts)){
                continue;
            }
            int totalAmount = availableProducts.stream().mapToInt(OrderProductDTO::getPrice).sum();
            Discount discount = DiscountStrategy.getDiscount(coupon.getDiscountType());
            if(discount.canUse(totalAmount, coupon)){
                map.put(coupon, availableProducts);
            }
        }
        return map;
    }
}
