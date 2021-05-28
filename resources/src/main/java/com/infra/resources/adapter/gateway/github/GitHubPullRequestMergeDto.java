package com.infra.resources.adapter.gateway.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GitHubPullRequestMergeDto {
    @JsonProperty("commit_title")
    private String commitTitle;
}
