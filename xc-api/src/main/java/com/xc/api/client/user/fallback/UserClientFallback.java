package com.xc.api.client.user.fallback;

import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public List<UserInfoResVO> getUserInfos(Iterable<Long> ids) {
                return null;
            }

            @Override
            public List<UserInfoResVO> getUserListByName(String name) {
                return null;
            }
        };
    }
}
