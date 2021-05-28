package com.infra.releases.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GitHubReleaseConfig {
    @Value("${github.owner}")
    private String githubOwner;

    @Value("${github.token}")
    private String githubToken;
}
