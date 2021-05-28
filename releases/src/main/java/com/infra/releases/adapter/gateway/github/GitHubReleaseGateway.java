package com.infra.releases.adapter.gateway.github;

import com.infra.releases.config.GitHubReleaseConfig;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GitHubReleaseGateway {

    private static final String HOSTNAME = "https://api.github.com";
    private static final String REPO_PATH = "/repos/";
    private static final String WORKFLOW_FILE_NAME = "push-main.yml";

    private final RestTemplate restTemplate;
    private final GitHubReleaseConfig gitHubConfig;

    public void createRelease(String repoName, String version) {
        HttpEntity<WorkflowDispatchDTO> entity = new HttpEntity<>(getWorkflowDispatchBody(version), getHeaders());
        restTemplate.exchange(getURI(getReleaseUri(repoName), Collections.emptyMap()), HttpMethod.POST, entity,
            String.class);
    }

    private String getReleaseUri(String repoName) {
        return REPO_PATH + gitHubConfig.getGithubOwner() + "/" + repoName + "/actions/workflows/" + WORKFLOW_FILE_NAME
            + "/dispatches";
    }

    private URI getURI(String uri, Map<String, String> queryParams) {
        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(HOSTNAME + uri);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url = url.queryParam(entry.getKey(), entry.getValue());
        }

        return url.build()
                  .toUri();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + gitHubConfig.getGithubToken());
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }

    private WorkflowDispatchDTO getWorkflowDispatchBody(String versionName) {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("tagName", versionName);

        return WorkflowDispatchDTO.builder()
                                  .ref("main")
                                  .inputs(inputMap)
                                  .build();
    }
}
