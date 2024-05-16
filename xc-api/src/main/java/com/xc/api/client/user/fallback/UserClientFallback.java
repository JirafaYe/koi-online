package com.xc.api.client.user.fallback;

import com.xc.api.client.user.UserClient;
import com.xc.api.dto.user.LongIdsVO;
import com.xc.api.dto.user.UserInfoResVO;
import com.xc.common.domain.dto.CommonLongIdDTO;
import com.xc.common.utils.CollUtils;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public List<UserInfoResVO> getUserInfos(LongIdsVO vo) {
                return null;
            }
        };
    }
}
