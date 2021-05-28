package com.infra.resources.adapter.gateway.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GitHubPullRequestStatusResponse {

    private String state;

    @JsonProperty("target_url")
    private String targetUrl;

    @JsonProperty("updated_at")
    private String updatedAt;
}
