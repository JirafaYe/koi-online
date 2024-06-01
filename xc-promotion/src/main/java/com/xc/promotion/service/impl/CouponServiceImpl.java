package com.xc.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.product.ProductClient;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.utils.*;
import com.xc.promotion.constants.PromotionConstants;
import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.domain.dto.CouponIssueFormDTO;
import com.xc.promotion.domain.enums.CouponStatus;
import com.xc.promotion.domain.enums.ObtainType;
import com.xc.promotion.domain.enums.UserCouponStatus;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.CouponScope;
import com.xc.promotion.domain.po.UserCoupon;
import com.xc.promotion.domain.query.CouponQuery;
import com.xc.promotion.domain.vo.CouponDetailVo;
import com.xc.promotion.domain.vo.CouponPageVO;
import com.xc.promotion.domain.vo.CouponScopeVO;
import com.xc.promotion.domain.vo.CouponVO;
import com.xc.promotion.mapper.CouponMapper;
import com.xc.promotion.service.ICouponScopeService;
import com.xc.promotion.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.promotion.service.IExchangeCodeService;
import com.xc.promotion.service.IUserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xc.promotion.constants.PromotionConstants.COUPON_CACHE_KEY_PREFIX;

/**
 * <p>
 * 优惠券的规则信息 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {


    private final IExchangeCodeService codeService;

    private final ICouponScopeService scopeService;

    private final IUserCouponService userCouponService;

    private final ProductClient productClient;

    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public void saveCoupon(CouponFormDTO dto) {
        Coupon coupon = BeanUtils.copyBean(dto, Coupon.class);
        save(coupon);
        if(!dto.getSpecific()){
            return;
        }
        Long couponId = coupon.getId();
        List<Long> scopes = dto.getScopes();
        if(CollUtils.isEmpty(scopes)){
            throw new BadRequestException("限定范围不能为空");
        }
        List<CouponScope> list = scopes.stream()
                .map(bizId -> new CouponScope().setCouponId(couponId).setBizId(bizId))
                .collect(Collectors.toList());
        scopeService.saveBatch(list);
    }

    @Override
    public PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query) {
        Page<Coupon> page = lambdaQuery()
                .eq(query.getStatus() != null, Coupon::getStatus, query.getStatus())
                .eq(query.getType() != null, Coupon::getDiscountType, query.getType())
                .eq(StringUtils.isNotBlank(query.getName()), Coupon::getName, query.getName())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<Coupon> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        List<CouponPageVO> voList = BeanUtils.copyList(records, CouponPageVO.class);
        return PageDTO.of(page, voList);
    }

    @Override
    @Transactional
    public void beginIssue(CouponIssueFormDTO dto) {
        Coupon coupon = getById(dto.getId());
        if(coupon == null){
            throw new BadRequestException("优惠券不存在");
        }
        //判断优惠券状态是否为暂停或待发放
        if(coupon.getStatus() != CouponStatus.DRAFT && coupon.getStatus() != CouponStatus.PAUSE){
            throw new BizIllegalException("优惠券状态错误");
        }
        //判断优惠券是不是立刻发放
        LocalDateTime issueBeginTime = dto.getIssueBeginTime();
        LocalDateTime now = LocalDateTime.now();
        boolean isBegin = issueBeginTime == null || !issueBeginTime.isAfter(now);

        //更新优惠券
        Coupon c = BeanUtils.copyBean(dto, Coupon.class);
        if(isBegin){
            c.setIssueBeginTime(now);
            c.setStatus(CouponStatus.ISSUING);
        }else{
            c.setStatus(CouponStatus.UN_ISSUE);
        }
        updateById(c);

        //  添加缓存
        if(isBegin){
            coupon.setIssueEndTime(c.getIssueEndTime());
            coupon.setIssueBeginTime(c.getIssueBeginTime());
            cacheCouponInfo(coupon);
        }

        //判断是否需要生成兑换码
        if (coupon.getObtainWay() == ObtainType.ISSUE && coupon.getStatus() == CouponStatus.DRAFT){
            coupon.setIssueEndTime(c.getIssueEndTime());
            codeService.asyncGenerateCode(coupon);
        }
    }

    private void cacheCouponInfo(Coupon coupon) {
        Map<String, String> map = new HashMap<>(4);
        map.put("issueBeginTime", String.valueOf(DateUtils.toEpochMilli(coupon.getIssueBeginTime())));
        map.put("issueEndTime", String.valueOf(DateUtils.toEpochMilli(coupon.getIssueEndTime())));
        map.put("totalNum", String.valueOf(coupon.getTotalNum()));
        map.put("userLimit", String.valueOf(coupon.getUserLimit()));

        redisTemplate.opsForHash().putAll(COUPON_CACHE_KEY_PREFIX + coupon.getId(), map);
    }

    @Override
    public CouponDetailVo queryCouponById(Long id) {
        Coupon coupon = getById(id);
        CouponDetailVo vo = BeanUtils.copyBean(coupon, CouponDetailVo.class);
        if(vo == null || !coupon.getSpecific()){
            return vo;
        }
        List<CouponScope> scopes = scopeService.lambdaQuery().eq(CouponScope::getCouponId, id).list();
        if(CollUtils.isEmpty(scopes)){
            return vo;
        }
        //  根据分类id 获取分类名称 如果是二级分类就用/将一级和二级拼接起来
        List<CouponScopeVO> scopeVos = scopes.stream()
                .map(CouponScope::getBizId)
                .map(c -> new CouponScopeVO(c, productClient.queryCategoryById(c)))
                .collect(Collectors.toList());
        vo.setScopes(scopeVos);
        return vo;
    }

    @Override
    public void deleteById(Long id) {
        Coupon coupon = getById(id);
        if(coupon == null  || coupon.getStatus() != CouponStatus.DRAFT){
            throw new BadRequestException("优惠券不存在或者优惠券正在使用中");
        }
        boolean success = remove(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getId, id)
                .eq(Coupon::getStatus, CouponStatus.DRAFT));
        if(!success){
            throw new BadRequestException("优惠券不存在或者优惠券正在使用中");
        }
        if(!coupon.getSpecific()){
            return;
        }
        scopeService.remove(new LambdaQueryWrapper<CouponScope>().eq(CouponScope::getCouponId, id));
    }

    @Override
    @Transactional
    public void updateCoupon(CouponFormDTO dto) {
        Long id = dto.getId();
        Coupon c = getById(id);
        if (c.getStatus() != CouponStatus.DRAFT){
            throw new BadRequestException("优惠券不是待发放");
        }
        if(c.getSpecific()){
            scopeService.remove(new LambdaQueryWrapper<CouponScope>()
                    .eq(CouponScope::getCouponId, id));
        }
        removeById(id);
        Coupon coupon = BeanUtils.copyBean(dto, Coupon.class);
        save(coupon);
        if(!coupon.getSpecific()){
            return;
        }
        List<Long> scopes = dto.getScopes();
        if(CollUtils.isEmpty(scopes)){
            throw new BadRequestException("限定范围不能为空");
        }
        List<CouponScope> list = scopes.stream()
                .map(bizId -> new CouponScope().setCouponId(id).setBizId(bizId))
                .collect(Collectors.toList());
        scopeService.saveBatch(list);
    }

    @Override
    @Transactional
    public void pauseIssue(Long id) {
        Coupon coupon = getById(id);
        if(coupon == null){
            throw new BadRequestException("优惠券不存在");
        }
        CouponStatus status = coupon.getStatus();
        if(status != CouponStatus.ISSUING && status != CouponStatus.UN_ISSUE){
            return;
        }
        boolean success = lambdaUpdate()
                .eq(Coupon::getId, id)
                .set(Coupon::getStatus, CouponStatus.PAUSE)
                .in(Coupon::getStatus, CouponStatus.ISSUING, CouponStatus.UN_ISSUE)
                .update();
        if(!success){
            throw new BadRequestException("优惠券已暂停");
        }

        //  删除缓存
        redisTemplate.delete(COUPON_CACHE_KEY_PREFIX + id);
    }

    @Override
    public List<CouponVO> queryIssuingCoupon() {
        List<Coupon> coupons = lambdaQuery()
                .eq(Coupon::getStatus, CouponStatus.ISSUING)
                .eq(Coupon::getObtainWay, ObtainType.PUBLIC)
                .list();
        if(CollUtils.isEmpty(coupons)){
            return CollUtils.emptyList();
        }
        List<Long> couponIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<UserCoupon> userCoupons = userCouponService.lambdaQuery()
                .eq(UserCoupon::getUserId, UserContext.getUser())
                .in(UserCoupon::getCouponId, couponIds)
                .list();
        Map<Long, Long> issuedMap = userCoupons.stream()
                .collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));
        Map<Long, Long> unUsedMap = userCoupons.stream()
                .filter(uc -> uc.getStatus() == UserCouponStatus.USED)
                .collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));
        List<CouponVO> list = new ArrayList<>(coupons.size());
        for (Coupon c : coupons){
            CouponVO vo = BeanUtils.copyBean(c, CouponVO.class);
            vo.setAvailable(c.getIssueNum() < c.getTotalNum()
                    && issuedMap.getOrDefault(c.getId(), 0L) < c.getUserLimit());
            vo.setReceived(unUsedMap.getOrDefault(c.getId(), 0L) > 0);
            list.add(vo);
        }
        return list;
    }

    @Override
    @Transactional
    public void beginIssueBatch(List<Coupon> coupons) {
        for (Coupon coupon : coupons) {
            coupon.setStatus(CouponStatus.ISSUING);
        }
        updateBatchById(coupons);

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection src = (StringRedisConnection) connection;
            for (Coupon coupon : coupons) {
                // 2.1.组织数据
                Map<String, String> map = new HashMap<>(4);
                map.put("issueBeginTime", String.valueOf(DateUtils.toEpochMilli(coupon.getIssueBeginTime())));
                map.put("issueEndTime", String.valueOf(DateUtils.toEpochMilli(coupon.getIssueEndTime())));
                map.put("totalNum", String.valueOf(coupon.getTotalNum()));
                map.put("userLimit", String.valueOf(coupon.getUserLimit()));
                // 2.2.写缓存
                src.hMSet(PromotionConstants.COUPON_CACHE_KEY_PREFIX + coupon.getId(), map);
            }
            return null;
        } );
    }

    @Override
    public Integer getNum() {
        return lambdaQuery().eq(Coupon::getStatus, CouponStatus.DRAFT).count();
    }

    @Override
    @Transactional
    public void couponOverBatch(List<Coupon> coupons) {
        for (Coupon coupon : coupons) {
            coupon.setStatus(CouponStatus.FINISHED);
        }
        List<String> couponIds = coupons.stream().map(c -> {
            return COUPON_CACHE_KEY_PREFIX + c.getId();
        }).collect(Collectors.toList());
        redisTemplate.delete(couponIds);
        updateBatchById(coupons);
    }
}
