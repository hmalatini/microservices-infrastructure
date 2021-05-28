package com.infra.resources.adapter.gateway.github;

import com.infra.resources.config.GitHubConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GitHubGateway {

    private static final String HOSTNAME = "https://api.github.com";
    private static final String REPO_PATH = "/repos/";

    private final RestTemplate restTemplate;
    private final GitHubConfig gitHubConfig;

    public void createRepo(String repoName) {
        HttpEntity<GitHubRepositoryDto> entity = new HttpEntity<>(getRepositoryBody(repoName), getHeaders());
        restTemplate.exchange(getURI("/user/repos", Collections.emptyMap()), HttpMethod.POST, entity, String.class);
    }

    public void deleteRepo(String repoName) {
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        restTemplate.exchange(getURI(REPO_PATH + gitHubConfig.getGithubOwner() + "/" + repoName, Collections.emptyMap()), HttpMethod.DELETE, entity, String.class);
    }

    public GitHubPullRequestCreationResponse createPR(String repo, String title, String fromBranch) {
        HttpEntity<GitHubPullRequestDto> entity = new HttpEntity<>(getPullRequestBody(title, fromBranch), getHeaders());
        ResponseEntity<GitHubPullRequestCreationResponse> response = restTemplate.exchange(getURI(REPO_PATH + gitHubConfig.getGithubOwner() + "/" + repo + "/pulls", Collections.emptyMap()), HttpMethod.POST, entity, GitHubPullRequestCreationResponse.class);
        return response.getBody();
    }

    public List<GitHubPullRequestStatusResponse> getCommitStatus(String repo, String commitSha) {
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<List<GitHubPullRequestStatusResponse>> response = restTemplate.exchange(getURI(REPO_PATH + gitHubConfig.getGithubOwner() + "/" + repo + "/statuses/" + commitSha, Collections.emptyMap()), HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
        return response.getBody();
    }

    public void mergePR(String repo, long number) {
        HttpEntity<GitHubPullRequestMergeDto> entity = new HttpEntity<>(getPRMergeBody(), getHeaders());
        restTemplate.exchange(getURI(REPO_PATH + gitHubConfig.getGithubOwner() + "/" + repo + "/pulls/" + number + "/merge", Collections.emptyMap()), HttpMethod.PUT, entity, String.class);
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
        headers.set("Authorization", "token " + gitHubConfig.getGithubToken());
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }

    private GitHubRepositoryDto getRepositoryBody(String name) {
        GitHubRepositoryDto repositoryDto = new GitHubRepositoryDto();
        repositoryDto.setName(name);
        repositoryDto.setPrivate(true);
        repositoryDto.setAutoInit(true);
        repositoryDto.setDeleteBranchOnMerge(true);

        return repositoryDto;
    }

    private GitHubPullRequestDto getPullRequestBody(String title, String fromBranch) {
        GitHubPullRequestDto dto = new GitHubPullRequestDto();
        dto.setTitle(title);
        dto.setHead(fromBranch);
        dto.setBase("main");
        return dto;
    }

    private GitHubPullRequestMergeDto getPRMergeBody() {
        GitHubPullRequestMergeDto dto = new GitHubPullRequestMergeDto();
        dto.setCommitTitle("merge code from infra repository");
        return dto;
    }
}
