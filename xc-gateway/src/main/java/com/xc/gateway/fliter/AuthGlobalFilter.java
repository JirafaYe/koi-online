package com.xc.gateway.fliter;


import com.xc.common.constants.JwtConstant;
import com.xc.common.exceptions.CommonException;
import com.xc.common.exceptions.UnauthorizedException;
import com.xc.common.utils.StringUtils;
import com.xc.gateway.properties.AuthProperties;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.JwtTokenUtils;
import com.xc.common.utils.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取Request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否不需要拦截
        if(isExclude(request.getPath().toString())){
            // 无需拦截，直接放行
            return chain.filter(exchange);
        }
        // 3.获取请求头中的token
        String token = null;
        List<String> headers = request.getHeaders().get("token");
        if (!CollUtils.isEmpty(headers)) {
            token = headers.get(0);
        }
        if(StringUtils.isEmpty(token)){
            throw new UnauthorizedException("无权限");
        }
        // 4.校验并解析token
        Long userId = null;
        try {
            Jws<Claims> claimsJws = JwtTokenUtils.parseJwt(token);
            Claims parseToken = claimsJws.getPayload();
            userId = (Long)parseToken.get(JwtConstant.USER_ID);
        } catch (UnauthorizedException e) {
            // 如果无效，拦截
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }
        if(userId == null){
          throw new CommonException("未登录");
        }
        Long finalUserId = userId;
        exchange.mutate().request(builder -> builder.header(JwtConstant.USER_HEADER, finalUserId.toString())).build();
        //  5.如果有效，传递用户信息
        UserContext.setUser(userId);
        // 6.放行
        return chain.filter(exchange);
    }

    /**
     * 路径匹配
     * @param antPath
     * @return
     */
    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, antPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}