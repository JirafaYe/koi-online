package com.xc.api.client.user;

import com.xc.api.client.user.fallback.UserClientFallback;
import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "user-service", fallbackFactory = UserClientFallback.class)
public interface UserClient {

    /**
     * 获取用户信息
     */
    @GetMapping("admin/getUserInfos")
    List<UserInfoResVO> getUserInfos(@RequestParam("ids") Iterable<Long> ids);

}
