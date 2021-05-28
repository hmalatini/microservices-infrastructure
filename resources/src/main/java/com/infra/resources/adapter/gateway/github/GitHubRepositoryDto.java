package com.infra.resources.adapter.gateway.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GitHubRepositoryDto {
    private String name;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("auto_init")
    private boolean autoInit;

    @JsonProperty("delete_branch_on_merge")
    private boolean deleteBranchOnMerge;
}
