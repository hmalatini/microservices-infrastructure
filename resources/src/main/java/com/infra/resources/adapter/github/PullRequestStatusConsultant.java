package com.infra.resources.adapter.github;

import com.infra.resources.adapter.gateway.github.GitHubGateway;
import com.infra.resources.adapter.gateway.github.GitHubPullRequestStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class PullRequestStatusConsultant {

    private final GitHubGateway gitHubGateway;

    PRStatus getStatus(String repoName, String commitSha) {
        List<GitHubPullRequestStatusResponse> statusResponses = gitHubGateway.getCommitStatus(repoName, commitSha);
        Collection<GitHubPullRequestStatusResponse> distinctResponses = getDistinctResponses(statusResponses);

        return getPRStatus(distinctResponses);
    }

    private PRStatus getPRStatus(Collection<GitHubPullRequestStatusResponse> distinctResponses) {
        if (isError(distinctResponses)) return PRStatus.ERROR;
        if (isSuccess(distinctResponses)) return PRStatus.SUCCESS;
        return PRStatus.PENDING;
    }

    boolean isError(Collection<GitHubPullRequestStatusResponse> responses) {
        return responses.stream().map(GitHubPullRequestStatusResponse::getState).anyMatch(status -> status.equals("error") || status.equals("failure"));
    }

    boolean isSuccess(Collection<GitHubPullRequestStatusResponse> responses) {
        return responses.stream().map(GitHubPullRequestStatusResponse::getState).allMatch(status -> status.equals("success"));
    }

    Collection<GitHubPullRequestStatusResponse> getDistinctResponses(List<GitHubPullRequestStatusResponse> statusResponses) {
        Map<String, GitHubPullRequestStatusResponse> responseMap = statusResponses.stream().collect(Collectors.toMap(
                GitHubPullRequestStatusResponse::getTargetUrl,
                status -> status,
                (existing, replacement) -> existing.getUpdatedAt().compareTo(replacement.getUpdatedAt()) > 0 ? existing : replacement
        ));

        return responseMap.values();
    }
}