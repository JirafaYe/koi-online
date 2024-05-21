package com.xc.api.client.log;

import com.xc.api.client.log.fallback.LogClientFallback;
import com.xc.api.dto.log.req.IogInfoReqDTO;
import com.xc.api.dto.log.req.LogInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "log-service", fallbackFactory = LogClientFallback.class)
public interface LogClient {
    /**
     * 保存日志
     * @param
     */
    @PostMapping("/logInfo/saveLog")
     void saveLog(LogInfo logInfo);
}
