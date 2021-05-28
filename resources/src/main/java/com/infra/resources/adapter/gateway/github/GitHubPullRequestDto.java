package com.infra.resources.adapter.gateway.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GitHubPullRequestDto {

    private String title;
    private String head;
    private String base;
}
