package com.infra.deployment.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DeploymentGitHubConfig {
    @Value("${github.owner}")
    private String githubOwner;

    @Value("${github.token}")
    private String githubToken;
}
