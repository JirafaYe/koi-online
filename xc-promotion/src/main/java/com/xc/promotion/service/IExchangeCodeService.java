package com.xc.promotion.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.po.Coupon;
import com.xc.promotion.domain.po.ExchangeCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.promotion.domain.query.CodeQuery;
import com.xc.promotion.domain.vo.ExchangeCodeVo;

/**
 * <p>
 * 兑换码 服务类
 * </p>
 *
 * @author Koi
 * @since 2024-05-15
 */
public interface IExchangeCodeService extends IService<ExchangeCode> {

    void asyncGenerateCode(Coupon coupon);

    PageDTO<ExchangeCodeVo> queryCodePage(CodeQuery query);
}
