package com.infra.resources.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TerraformCloudConfig {
    @Value("${terraform.user}")
    private String user;

    @Value("${terraform.token}")
    private String token;

    @Value("${terraform.org}")
    private String organization;

    @Value("${terraform.vsc-token}")
    private String vscToken;
}
