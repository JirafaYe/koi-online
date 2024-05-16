package com.xc.promotion.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class PromotionConfig {

    @Bean
    public Executor generateExchangeCodeExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(2);
        //最大线程池大小
        executor.setMaxPoolSize(5);
        //线程池队列
        executor.setQueueCapacity(200);
        //线程名称
        executor.setThreadNamePrefix("exchange-code-handler-");
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
