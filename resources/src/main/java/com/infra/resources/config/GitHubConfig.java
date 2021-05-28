package com.infra.resources.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GitHubConfig {
    @Value("${github.owner}")
    private String githubOwner;

    @Value("${github.token}")
    private String githubToken;
}
