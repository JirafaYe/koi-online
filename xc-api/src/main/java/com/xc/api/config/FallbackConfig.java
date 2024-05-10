package com.xc.api.config;

import com.xc.api.client.firmad.fallback.FirmadClientFallback;
import com.xc.api.client.log.fallback.LogClientFallback;
import com.xc.api.client.media.fallback.MediaClientFallback;
import com.xc.api.client.product.fallback.ProductClientFallback;
import com.xc.api.client.promotion.fallback.PromotionClientFallback;
import com.xc.api.client.remark.fallback.RemarkClientFallback;
import com.xc.api.client.search.fallback.SearchClientFallback;
import com.xc.api.client.trade.fallback.TradeClientFallback;
import com.xc.api.client.user.fallback.UserClientFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallbackConfig {
    @Bean
    public FirmadClientFallback firmadClientFallback(){
        return new FirmadClientFallback();
    }

    @Bean
    public LogClientFallback logClientFallback(){
        return new LogClientFallback();
    }

    @Bean
    public MediaClientFallback mediaClientFallback(){
        return new MediaClientFallback();
    }

    @Bean
    public ProductClientFallback productClientFallback(){
        return new ProductClientFallback();
    }

    @Bean
    public PromotionClientFallback promotionClientFallback(){
        return new PromotionClientFallback();
    }

    @Bean
    public RemarkClientFallback remarkClientFallback(){
        return new RemarkClientFallback();
    }

    @Bean
    public SearchClientFallback searchClientFallback(){
        return new SearchClientFallback();
    }

    @Bean
    public TradeClientFallback tradeClientFallback(){
        return new TradeClientFallback();
    }

    @Bean
    public UserClientFallback userClientFallback(){
        return new UserClientFallback();
    }

}
