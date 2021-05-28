package com.infra.releases.core.usecase;

import com.infra.releases.adapter.gateway.github.GitHubReleaseGateway;
import com.infra.releases.adapter.gateway.microservices.MicroservicesGateway;
import com.infra.releases.core.domain.Microservice;
import com.infra.releases.core.domain.Release;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateRelease {

    private final MicroservicesGateway microservicesGateway;
    private final GitHubReleaseGateway gitHubReleaseGateway;

    public void execute(String microserviceName, Release release) {
        Microservice microservice = microservicesGateway.getMicroservice(microserviceName);
        String repoName = getRepoName(microservice);

        gitHubReleaseGateway.createRelease(repoName, release.getName());
    }

    private String getRepoName(Microservice microservice) {
        return microservice.getGitUrl()
                           .substring(microservice.getGitUrl()
                                                  .lastIndexOf('/') + 1);
    }
}
