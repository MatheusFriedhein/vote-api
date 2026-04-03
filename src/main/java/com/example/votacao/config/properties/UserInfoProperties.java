package com.example.votacao.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.user-info")
public record UserInfoProperties(
        boolean enabled,
        String baseUrl,
        int connectTimeoutMs,
        int readTimeoutMs
) {
}
