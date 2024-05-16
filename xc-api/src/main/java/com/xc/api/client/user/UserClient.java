package com.xc.api.client.user;

import com.xc.api.client.user.fallback.UserClientFallback;
import com.xc.api.dto.user.LongIdsVO;
import com.xc.api.dto.user.UserInfoResVO;
import com.xc.common.domain.dto.CommonLongIdDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "user-service", fallbackFactory = UserClientFallback.class)
public interface UserClient {

    /**
     * 获取用户信息
     */
    @PostMapping("/admin/getUserInfos")
    List<UserInfoResVO> getUserInfos(@RequestBody LongIdsVO vo);
}
