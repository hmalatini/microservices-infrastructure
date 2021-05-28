package com.infra.resources.adapter.gateway.terraformcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VCSRepoWorkspaceCreationDto {
    private String identifier;
    @JsonProperty("oauth-token-id")
    private String oauthToken;
    private String branch;
}
