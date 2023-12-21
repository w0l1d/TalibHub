package org.ilisi.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(
        String key,
        Long accessTokenExpirationInMinutes,
        Long refreshTokenExpirationInDays
) {

}