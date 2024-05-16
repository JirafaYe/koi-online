package com.xc.promotion.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.query.CodeQuery;
import com.xc.promotion.domain.vo.ExchangeCodeVo;
import com.xc.promotion.service.IExchangeCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 管理端兑换码
 *
 * @author Koi
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/codes")
@RequiredArgsConstructor
public class ExchangeCodeController {

    private final IExchangeCodeService codeService;

    @GetMapping("/page")
    public PageDTO<ExchangeCodeVo> queryCodePage(CodeQuery query){
        return codeService.queryCodePage(query);
    }

}
