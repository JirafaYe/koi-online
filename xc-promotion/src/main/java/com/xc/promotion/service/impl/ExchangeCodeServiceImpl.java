package com.xc.promotion.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.utils.CollUtils;
import com.xc.promotion.constants.PromotionConstants;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.ExchangeCode;
import com.xc.promotion.domain.query.CodeQuery;
import com.xc.promotion.domain.vo.ExchangeCodeVo;
import com.xc.promotion.mapper.ExchangeCodeMapper;
import com.xc.promotion.service.IExchangeCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.promotion.utils.CodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.xc.promotion.constants.PromotionConstants.COUPON_CODE_MAP_KEY;
import static com.xc.promotion.constants.PromotionConstants.COUPON_RANGE_KEY;

/**
 * <p>
 * 兑换码 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
@Service
@Slf4j
public class ExchangeCodeServiceImpl extends ServiceImpl<ExchangeCodeMapper, ExchangeCode> implements IExchangeCodeService {

    private final BoundValueOperations<String, String> serialOps;

    private final StringRedisTemplate redisTemplate;

    public ExchangeCodeServiceImpl(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.serialOps = redisTemplate.boundValueOps(PromotionConstants.COUPON_CODE_SERIAL_KEY);
    }


    @Override
    @Async("generateExchangeCodeExecutor")
    public void asyncGenerateCode(Coupon coupon) {
        //获取发放数量
        Integer totalNum = coupon.getTotalNum();
        //redis获取自增序列号
        Long result = serialOps.increment(totalNum);
        if(result == null){
            log.error("获取序列号失败");
            return;
        }
        int maxSerialNum = result.intValue();
        List<ExchangeCode> list = new ArrayList<>(totalNum);
        for (int seriaNum = maxSerialNum - totalNum + 1; seriaNum <= maxSerialNum; seriaNum++){
            //生成兑换码
            String code = CodeUtil.generateCode(seriaNum, coupon.getId());
            ExchangeCode e = new ExchangeCode();
            e.setCode(code);
            e.setId(seriaNum);
            e.setExchangeTargetId(coupon.getId());
            e.setExpiredTime(coupon.getIssueEndTime());
            list.add(e);
        }
        saveBatch(list);
        //写入 缓存
        redisTemplate.opsForZSet().add(COUPON_RANGE_KEY, coupon.getId().toString(), maxSerialNum);
    }

    @Override
    public PageDTO<ExchangeCodeVo> queryCodePage(CodeQuery query) {
        Page<ExchangeCode> page = lambdaQuery()
                .eq(ExchangeCode::getExchangeTargetId, query.getCouponId())
                .eq(ExchangeCode::getStatus, query.getStatus())
                .page(query.toMpPage());
        return PageDTO.of(page, c -> new ExchangeCodeVo(c.getId(), c.getCode()));
    }

    @Override
    public boolean updateExchangeMark(long serialNum, boolean mark) {
        Boolean success = redisTemplate.opsForValue().setBit(COUPON_CODE_MAP_KEY, serialNum, mark);
        return success != null && success;
    }

    @Override
    public Long exchangeTargetId(long serialNum) {
        Set<String> range = redisTemplate.opsForZSet().rangeByScore(COUPON_RANGE_KEY, serialNum, serialNum + 5000, 0L, 1L);
        if(CollUtils.isEmpty(range)){
            return null;
        }
        String next = range.iterator().next();
        return Long.parseLong(next);
    }
}
