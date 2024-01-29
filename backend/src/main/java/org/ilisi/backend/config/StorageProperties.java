package org.ilisi.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public record StorageProperties(String location) {
}