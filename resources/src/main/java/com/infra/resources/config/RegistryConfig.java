package com.infra.resources.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RegistryConfig {
    @Value("${docker-registry.user}")
    private String registryUser;

    @Value("${docker-registry.repo}")
    private String registryRepo;

    @Value("${docker-registry.token}")
    private String dockerRegistryToken;
}
