package com.xc.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 网关权限校验路径
 */
@Data
@ConfigurationProperties(prefix = "xc.auth")
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
