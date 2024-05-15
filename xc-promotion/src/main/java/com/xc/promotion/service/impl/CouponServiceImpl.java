package com.xc.promotion.service.impl;

import com.xc.common.exceptions.BadRequestException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.CouponScope;
import com.xc.promotion.mapper.CouponMapper;
import com.xc.promotion.service.ICouponScopeService;
import com.xc.promotion.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private ICouponScopeService scopeService;

    @Override
    @Transactional
    public void saveCoupon(CouponFormDTO dto) {
        Long userId = UserContext.getUser();
        log.info("{}",userId);
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
}
