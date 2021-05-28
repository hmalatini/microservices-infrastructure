package com.infra.resources.adapter.github;

import com.infra.resources.adapter.gateway.github.GitHubGateway;
import com.infra.resources.adapter.gateway.github.GitHubPullRequestCreationResponse;
import com.infra.resources.core.exceptions.GitHubRepositoryException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class GitHubPullRequestCreatorAndMerger {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubPullRequestCreatorAndMerger.class);

    private static final int RESOURCE_CHECK_TIME_OUT = 900;
    private static final int RESOURCE_CHECK_INTERVAL_TIME = 5;

    private final GitHubGateway gitHubGateway;
    private final ExecutorService executorService;
    private final PullRequestStatusConsultant pullRequestStatusConsultant;

    public void createAndMergeIfApproved(String repoName, String title, String branchName) {
        GitHubPullRequestCreationResponse pr = gitHubGateway.createPR(repoName, title, branchName);
        this.executorService.submit(() -> checkCreation(pr, repoName));
    }

    private void checkCreation(GitHubPullRequestCreationResponse pr, String repoName) {
        Instant startInstant = Instant.now();
        Instant checkRunInstant = Instant.now();

        String commitSha = pr.getStatusesUrl().substring(pr.getStatusesUrl().lastIndexOf('/') + 1);
        PRStatus status = PRStatus.PENDING;

        while (!status.equals(PRStatus.SUCCESS)) {
            Instant current = Instant.now();

            isError(pr.getNumber(), repoName, status);
            isTimeOut(pr.getNumber(), repoName, startInstant, current);

            if (shouldCheckStatus(checkRunInstant, current, RESOURCE_CHECK_INTERVAL_TIME)) {
                status = getPrStatus(pr.getNumber(), repoName, commitSha);
                checkRunInstant = current;
            }
        }

        mergePR(pr.getNumber(), repoName);
    }

    private PRStatus getPrStatus(Long prNumber, String repoName, String commitSha) {
        PRStatus status = pullRequestStatusConsultant.getStatus(repoName, commitSha);
        LOGGER.debug("[repo:{}] [pr_number:{}] Creating resource. Status of pull request is {}", repoName, prNumber, status);
        return status;
    }

    private boolean shouldCheckStatus(Instant checkRunInstant, Instant current, int intervalTime) {
        return Duration.between(checkRunInstant, current).getSeconds() > intervalTime;
    }

    private void isError(Long prNumber, String repoName, PRStatus status) {
        if (status.equals(PRStatus.ERROR)) {
            LOGGER.error("[repo:{}] [pr_number:{}] Error: some requirements in PR failed", repoName, prNumber);
            throw new GitHubRepositoryException(String.format("Requirements error on pr %d of repo %s", prNumber, repoName));
        }
    }

    private void isTimeOut(Long prNumber, String repoName, Instant startInstant, Instant current) {
        if (Duration.between(startInstant, current).getSeconds() > RESOURCE_CHECK_TIME_OUT) {
            LOGGER.error("[repo:{}] [pr_number:{}] Error creating resource. Time out reached", repoName, prNumber);
            throw new GitHubRepositoryException(String.format("Time out waiting for pr %d to be merged on repo %s", prNumber, repoName));
        }
    }

    private void mergePR(Long prNumber, String repoName) {
        gitHubGateway.mergePR(repoName, prNumber);
    }
}
