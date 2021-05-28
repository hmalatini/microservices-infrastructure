package com.infra.deployment.core.usecase;

import com.infra.deployment.adapter.deploymentyamlgenerator.HelmDeploymentCreator;
import com.infra.deployment.adapter.filemanager.DeploymentFileManager;
import com.infra.deployment.adapter.gateway.microservice.MicroserviceDTO;
import com.infra.deployment.adapter.gateway.microservice.MicroserviceGateway;
import com.infra.deployment.adapter.repository.DeploymentRepository;
import com.infra.deployment.adapter.vscmanager.DeploymentGitManager;
import com.infra.deployment.config.DeploymentGitHubConfig;
import com.infra.deployment.core.domain.Deployment;
import com.infra.deployment.core.exceptions.CreateDeploymentException;
import java.io.File;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeploymentExecutor {

    private final DeploymentGitManager gitManager;
    private final DeploymentFileManager fileManager;
    private final HelmDeploymentCreator deploymentCreator;
    private final DeploymentGitHubConfig deploymentGitHubConfig;
    private final MicroserviceGateway microserviceGateway;

    public void execute(Deployment deployment, String description) {
        MicroserviceDTO microservice = microserviceGateway.getMicroservice(deployment.getMicroserviceName());
        String path = gitManager.getRepositoryPrefixPath() + UUID.randomUUID();
        try {
            createDeployment(deployment, path, microservice, description);
        } catch (Exception e) {
            throw new CreateDeploymentException("error creating deployment", e);
        } finally {
            fileManager.deleteDir(path);
        }
    }

    private void createDeployment(Deployment deployment, String path, MicroserviceDTO microservice,
                                  String description) {
        File dir = fileManager.createDir(path);

        Git deploymentRepository = gitManager.cloneRepo(microservice.getDeploymentGitUrl() + ".git",
            deploymentGitHubConfig.getGithubOwner(), deploymentGitHubConfig.getGithubToken(), dir);

        deploymentCreator.createDeployment(deployment, dir);

        gitManager.addCommitAndPush(deploymentRepository, description, deploymentGitHubConfig.getGithubOwner(),
            deploymentGitHubConfig.getGithubToken());
    }
}
