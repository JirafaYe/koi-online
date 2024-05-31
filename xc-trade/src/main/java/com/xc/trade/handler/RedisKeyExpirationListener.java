package com.xc.trade.handler;

import com.xc.common.utils.ByteUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.constants.RedisConstants;
import com.xc.trade.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    IOrderService orderService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String keyExpired = message.toString();
        log.info("redis key expired: {}", keyExpired);
        if(keyExpired.startsWith(RedisConstants.ORDER_PREFIX)){
            String[] split = keyExpired.split(":");
            Long userId = Long.valueOf(split[split.length - 1]);
            Long orderId = Long.valueOf(split[split.length - 2]);
            UserContext.setUser(userId);
            boolean cancel = orderService.canceledOrder(orderId);
            log.info("取消订单{}",cancel);
        }
    }
}
