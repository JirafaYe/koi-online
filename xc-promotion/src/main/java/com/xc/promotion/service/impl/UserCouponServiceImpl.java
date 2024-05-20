package com.xc.promotion.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.DbException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.promotion.constants.PromotionConstants;
import com.xc.promotion.domain.dto.CouponDiscountDTO;
import com.xc.promotion.domain.dto.OrderProductDTO;
import com.xc.promotion.domain.enums.ExchangeCodeStatus;
import com.xc.promotion.domain.enums.UserCouponStatus;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.ExchangeCode;
import com.xc.promotion.domain.po.UserCoupon;
import com.xc.promotion.domain.query.UserCouponQuery;
import com.xc.promotion.domain.vo.CouponVO;
import com.xc.promotion.mapper.CouponMapper;
import com.xc.promotion.mapper.UserCouponMapper;
import com.xc.promotion.service.ICouponService;
import com.xc.promotion.service.IExchangeCodeService;
import com.xc.promotion.service.IUserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.promotion.strategy.discount.DiscountStrategy;
import com.xc.promotion.utils.CodeUtil;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户领取优惠券的记录，是真正使用的优惠券信息 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements IUserCouponService {

    private final CouponMapper couponMapper;

    private final StringRedisTemplate redisTemplate;

    private final IExchangeCodeService codeService;

    @Override
    public PageDTO<CouponVO> queryMyCoupon(UserCouponQuery query) {
        Long userId = UserContext.getUser();
        Page<UserCoupon> page = lambdaQuery()
                .eq(UserCoupon::getUserId, userId)
                .eq(query.getStatus() != null, UserCoupon::getStatus, query.getStatus())
                .page(query.toMpPage(new OrderItem("term_end_time", true)));
        List<UserCoupon> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        List<Long> couponIds = records.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        List<Coupon> coupons = couponMapper.selectBatchIds(couponIds);
        return PageDTO.of(page, BeanUtils.copyList(coupons, CouponVO.class));
    }

    @Override
    public void receiveCoupon(Long couponId) {
        Coupon coupon = queryCouponByCache(couponId);
        if(coupon == null){
            throw new BadRequestException("优惠券不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(coupon.getIssueBeginTime()) || now.isAfter(coupon.getIssueEndTime())){
            throw new BadRequestException("优惠券发放已经结束或者尚未开始");
        }
        if(coupon.getTotalNum() <= 0){
            throw new BadRequestException("优惠券库存不足");
        }
        Long userId = UserContext.getUser();
        coupon.setId(couponId);
        synchronized (userId.toString().intern()){
            IUserCouponService userCouponService = (IUserCouponService) AopContext.currentProxy();
            userCouponService.checkAndCreateUserCoupon(coupon, userId, null);
        }
    }

    @Override
    public void exchangeCode(String code) {
        long serialNum = CodeUtil.parseCode(code);
        boolean exchanged = codeService.updateExchangeMark(serialNum, true);
        if(exchanged){
            throw new BizIllegalException("兑换码已经被使用");
        }
        try {
            Long couponId = codeService.exchangeTargetId(serialNum);
            if(couponId == null){
                throw new BizIllegalException("优惠券不存在");
            }
            Coupon coupon = queryCouponByCache(couponId);
            if(coupon == null){
                throw new BizIllegalException("优惠券不存在");
            }
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(coupon.getIssueEndTime()) || now.isBefore(coupon.getIssueBeginTime())){
                throw new BizIllegalException("兑换码已经过期");
            }
            Long userId = UserContext.getUser();
            coupon.setId(couponId);
            synchronized (userId.toString().intern()){
                IUserCouponService userCouponService = (IUserCouponService) AopContext.currentProxy();
                userCouponService.checkAndCreateUserCoupon(coupon, userId, 1);
            }
        } catch (Exception e){
            codeService.updateExchangeMark(serialNum, false);
            throw e;
        }
    }

    @Override
    @Transactional
    public void writeOffCoupon(List<Long> userCouponIds) {
        List<UserCoupon> userCoupons = listByCouponIds(userCouponIds);
        if(CollUtils.isEmpty(userCoupons)){
            throw new BadRequestException("不存在优惠券");
        }
        List<UserCoupon> list = userCoupons.stream()
                .filter(coupon -> {
                    if (coupon == null) {
                        return false;
                    }
                    if (UserCouponStatus.UNUSED != coupon.getStatus()) {
                        return false;
                    }
                    LocalDateTime now = LocalDateTime.now();
                    return !now.isBefore(coupon.getTermBeginTime()) && !now.isAfter(coupon.getTermEndTime());
                })
                .map(coupon -> {
                    UserCoupon uc = new UserCoupon();
                    uc.setId(coupon.getId());
                    uc.setStatus(UserCouponStatus.USED);
                    return uc;
                })
                .collect(Collectors.toList());
        boolean success = updateBatchById(list);
        if(!success){
            throw new BizIllegalException("使用优惠券失败");
        }
        List<Long> couponIds = userCoupons.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        int res = couponMapper.incrUsedNum(couponIds, 1);
        if(res < 1){
            throw new DbException("更新优惠券使用数量失败！");
        }
    }

    @Override
    @Transactional
    public void refundCoupon(List<Long> userCouponIds) {
        List<UserCoupon> userCoupons = listByCouponIds(userCouponIds);
        if(CollUtils.isEmpty(userCoupons)){
            return;
        }
        List<UserCoupon> list = userCoupons.stream()
                .filter(coupon -> coupon != null && UserCouponStatus.USED == coupon.getStatus())
                .map(coupon -> {
                    UserCoupon c = new UserCoupon();
                    c.setId(coupon.getId());
                    LocalDateTime now = LocalDateTime.now();
                    UserCouponStatus status = now.isAfter(coupon.getTermEndTime()) ?
                            UserCouponStatus.EXPIRED : UserCouponStatus.UNUSED;
                    c.setStatus(status);
                    return c;
                })
                .collect(Collectors.toList());
        boolean success = updateBatchById(list);
        if(!success){
            throw new BizIllegalException("退还优惠券失败");
        }
        List<Long> couponIds = userCoupons.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        int res = couponMapper.incrUsedNum(couponIds, -1);
        if (res < 1) {
            throw new DbException("更新优惠券使用数量失败！");
        }
    }

    @Override
    public List<String> queryDiscountRules(List<Long> userCouponIds) {
        List<Coupon> coupons = baseMapper.queryCouponByUserCouponIds(userCouponIds, UserCouponStatus.USED);
        if(CollUtils.isEmpty(coupons)){
            return CollUtils.emptyList();
        }
        return coupons.stream()
                .map(c -> DiscountStrategy.getDiscount(c.getDiscountType()).getRule(c))
                .collect(Collectors.toList());
    }

    private List<UserCoupon> listByCouponIds(List<Long> userCouponIds) {
        return lambdaQuery()
                .eq(UserCoupon::getUserId, UserContext.getUser())
                .in(UserCoupon::getCouponId, userCouponIds)
                .list();
    }


    @Transactional
    @Override
    @Async
    public void checkAndCreateUserCoupon(Coupon coupon, Long userId, Integer serialNum) {
        Long couponId = coupon.getId();
        String key = PromotionConstants.USER_COUPON_CACHE_KEY_PREFIX + couponId;
        Long count = redisTemplate.opsForHash().increment(key, userId.toString(), 1);
        if(count > coupon.getUserLimit()){
            throw new BadRequestException("优惠券领取次数太多");
        }
        redisTemplate.opsForHash().increment(PromotionConstants.COUPON_CACHE_KEY_PREFIX + couponId, "totalNum", -1);

        Coupon c = couponMapper.selectById(couponId);
        //更新已经发放的数量
        int res = couponMapper.incrIssueNum(couponId);
        if(res == 0){
            throw new BizIllegalException("优惠券库存不足");
        }
        saveUserCoupon(c, userId);

        if(serialNum != null){
            // 修改兑换码状态
            codeService.lambdaUpdate()
                    .set(ExchangeCode::getUserId, userId)
                    .set(ExchangeCode::getStatus, ExchangeCodeStatus.USED)
                    .eq(ExchangeCode::getId, serialNum)
                    .update();
        }
    }

    private void saveUserCoupon(Coupon c, Long userId) {
        UserCoupon uc = new UserCoupon();
        uc.setCouponId(c.getId());
        uc.setUserId(userId);
        //有效期信息
        LocalDateTime termBeginTime = c.getTermBeginTime();
        LocalDateTime termEndTime = c.getTermEndTime();
        if(termBeginTime == null){
            termBeginTime = LocalDateTime.now();
            termEndTime = termBeginTime.plusDays(c.getTermDays());
        }
        uc.setTermBeginTime(termBeginTime);
        uc.setTermEndTime(termEndTime);
        save(uc);
    }

    private Coupon queryCouponByCache(Long couponId) {
        String key = PromotionConstants.COUPON_CACHE_KEY_PREFIX + couponId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries.isEmpty()){
            return null;
        }
        Map<String, String> couponMap = entries.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> (String) entry.getKey(),
                        entry -> (String) entry.getValue()
                ));

        // 创建Coupon对象并设置属性
        Coupon coupon = new Coupon();

        // 转换issueBeginTime
        if (couponMap.containsKey("issueBeginTime")) {
            long beginTimeMillis = Long.parseLong(couponMap.get("issueBeginTime"));
            Instant beginInstant = Instant.ofEpochMilli(beginTimeMillis);
            ZonedDateTime zdt = beginInstant.atZone(ZoneId.systemDefault()); // 使用系统默认时区
            coupon.setIssueBeginTime(zdt.toLocalDateTime());
        }

        // 类似地转换issueEndTime
        if (couponMap.containsKey("issueEndTime")) {
            long endTimeMillis = Long.parseLong(couponMap.get("issueEndTime"));
            Instant endInstant = Instant.ofEpochMilli(endTimeMillis);
            ZonedDateTime zdt = endInstant.atZone(ZoneId.systemDefault()); // 使用系统默认时区
            coupon.setIssueEndTime(zdt.toLocalDateTime());
        }
        if (couponMap.containsKey("totalNum")) {
            coupon.setTotalNum(Integer.parseInt(couponMap.get("totalNum")));
        }
        if (couponMap.containsKey("userLimit")) {
            coupon.setUserLimit(Integer.parseInt(couponMap.get("userLimit")));
        }
        return coupon;
    }
}
