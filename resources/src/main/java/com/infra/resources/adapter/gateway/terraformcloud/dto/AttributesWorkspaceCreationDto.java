package com.infra.resources.adapter.gateway.terraformcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AttributesWorkspaceCreationDto {
    private String name;
    @JsonProperty("terraform_version")
    private String terraformVersion;
    @JsonProperty("trigger-prefixes")
    private List<String> triggerPrefixes;
    @JsonProperty("working-directory")
    private String workingDirectory;
    @JsonProperty("vcs-repo")
    private VCSRepoWorkspaceCreationDto vcsRepo;
    @JsonProperty("auto-apply")
    private boolean autoApply;
}
