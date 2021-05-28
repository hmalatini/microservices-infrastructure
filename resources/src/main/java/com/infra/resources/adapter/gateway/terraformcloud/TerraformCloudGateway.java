package com.infra.resources.adapter.gateway.terraformcloud;

import com.infra.resources.adapter.gateway.terraformcloud.dto.*;
import com.infra.resources.adapter.gateway.terraformcloud.response.RunResponse;
import com.infra.resources.adapter.gateway.terraformcloud.response.WorkspaceCreationResponse;
import com.infra.resources.config.GitHubConfig;
import com.infra.resources.config.TerraformCloudConfig;
import com.infra.resources.core.domain.TerraformVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TerraformCloudGateway {
    private static final String HOSTNAME = "https://app.terraform.io/api/v2";

    private final RestTemplate restTemplate;
    private final TerraformCloudConfig terraformCloudConfig;
    private final GitHubConfig gitHubConfig;

    public String createWorkspace(String repoName, String workspaceName, String workingDir, boolean autoApply) {
        HttpEntity<WorkspaceCreationDto> entity = new HttpEntity<>(getWorkspaceBody(repoName, workspaceName, workingDir, autoApply), getHeaders());
        ResponseEntity<WorkspaceCreationResponse> responseEntity = restTemplate.exchange(getURI("/organizations/" + terraformCloudConfig.getOrganization() + "/workspaces", Collections.emptyMap()), HttpMethod.POST, entity, WorkspaceCreationResponse.class);
        return responseEntity.getBody().getData().getId();
    }

    public void deleteWorkspace(String workspaceName) {
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        restTemplate.exchange(getURI("/organizations/" + terraformCloudConfig.getOrganization() + "/workspaces/" + workspaceName, Collections.emptyMap()), HttpMethod.DELETE, entity, String.class);
    }

    public void createVariable(String workspaceId, TerraformVariable variable) {
        HttpEntity<VariableCreationDto> entity = new HttpEntity<>(getVariableBody(workspaceId, variable), getHeaders());
        restTemplate.exchange(getURI("/vars", Collections.emptyMap()), HttpMethod.POST, entity, String.class);
    }

    public String run(String workspaceId, String msg, boolean isDestroy) {
        HttpEntity<RunDto> entity = new HttpEntity<>(getRunBody(workspaceId, msg, isDestroy), getHeaders());
        ResponseEntity<RunResponse> responseEntity = restTemplate.exchange(getURI("/runs", Collections.emptyMap()), HttpMethod.POST, entity, RunResponse.class);
        return responseEntity.getBody().getData().getId();
    }

    public String getRunStatus(String runId) {
        HttpEntity<RunDto> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<RunResponse> responseEntity = restTemplate.exchange(getURI("/runs/" + runId, Collections.emptyMap()), HttpMethod.GET, entity, RunResponse.class);
        return responseEntity.getBody().getData().getAttributes().getStatus();
    }

    private RunDto getRunBody(String workspaceId, String msg, boolean isDestroy) {
        return RunDto.builder()
                .data(
                        DataRunDto.builder()
                                .type("runs")
                                .attributes(
                                        AttributesRunDto.builder()
                                                .message(msg)
                                                .isDestroy(isDestroy)
                                                .build()
                                )
                                .relationships(RelationshipsVariableCreationDto.builder()
                                        .workspace(
                                                WorkspaceVariableCreationDto.builder()
                                                        .data(
                                                                DataWorkspaceVariableCreationDto.builder()
                                                                        .type("workspaces")
                                                                        .id(workspaceId)
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build())
                                .build()
                )
                .build();
    }

    private VariableCreationDto getVariableBody(String workspaceId, TerraformVariable variable) {
        String category = variable.isEnvironment() ? "env" : "terraform";
        return VariableCreationDto.builder()
                .data(
                        DataVariableCreationDto.builder()
                                .type("vars")
                                .attributes(
                                        AttributesVariableCreationDto.builder()
                                                .key(variable.getKey())
                                                .value(variable.getValue())
                                                .description(variable.getDescription())
                                                .category(category)
                                                .sensitive(variable.isSensitive())
                                                .build())
                                .relationships(
                                        RelationshipsVariableCreationDto.builder()
                                                .workspace(
                                                        WorkspaceVariableCreationDto.builder()
                                                                .data(
                                                                        DataWorkspaceVariableCreationDto.builder()
                                                                                .id(workspaceId)
                                                                                .type("workspaces")
                                                                                .build())
                                                                .build())
                                                .build())
                                .build()
                )
                .build();
    }

    private URI getURI(String uri, Map<String, String> queryParams) {
        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(HOSTNAME + uri);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url = url.queryParam(entry.getKey(), entry.getValue());
        }

        return url.build().toUri();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + terraformCloudConfig.getToken());
        headers.set("Content-Type", "application/vnd.api+json");
        return headers;
    }

    private WorkspaceCreationDto getWorkspaceBody(String repoName, String workspaceName, String workingDirectory, boolean autoApply) {
        VCSRepoWorkspaceCreationDto vcsRepoDto = new VCSRepoWorkspaceCreationDto();
        vcsRepoDto.setIdentifier(gitHubConfig.getGithubOwner() + "/" + repoName);
        vcsRepoDto.setOauthToken(terraformCloudConfig.getVscToken());

        AttributesWorkspaceCreationDto attributesDto = new AttributesWorkspaceCreationDto();
        attributesDto.setName(workspaceName);
        attributesDto.setTerraformVersion("0.14.7");
        attributesDto.setTriggerPrefixes(Collections.emptyList());
        attributesDto.setWorkingDirectory(workingDirectory);
        attributesDto.setVcsRepo(vcsRepoDto);
        attributesDto.setAutoApply(autoApply);

        DataWorkspaceCreationDto dataDto = new DataWorkspaceCreationDto();
        dataDto.setType("workspaces");
        dataDto.setAttributes(attributesDto);

        WorkspaceCreationDto creationDto = new WorkspaceCreationDto();
        creationDto.setData(dataDto);

        return creationDto;
    }
}
