package com.infra.resources.core.usecase.microservice;

import com.infra.resources.adapter.controller.microservice.dto.MicroserviceCreation;
import com.infra.resources.adapter.gateway.github.GitHubGateway;
import com.infra.resources.adapter.infraresources.creation.CreationResourceManager;
import com.infra.resources.adapter.repository.MicroserviceRepository;
import com.infra.resources.config.GitHubConfig;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.MicroserviceAlreadyExistsException;
import com.infra.resources.core.usecase.cluster.GetCluster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMicroservice {

    private static final String GITHUB_URL = "https://github.com/";

    private final GitHubConfig gitHubConfig;
    private final GitHubGateway gitHubGateway;
    private final CreationResourceManager creationResourceManager;
    private final MicroserviceRepository repository;

    public Microservice execute(MicroserviceCreation creation) {
        checkIfExists(creation);

        gitHubGateway.createRepo(creation.getName() + "-infra");
        String workspaceId = creationResourceManager.createMicroservice(creation);
        return repository.save(getMicroserviceForCreation(creation, workspaceId));
    }

    private void checkIfExists(MicroserviceCreation creation) {
        repository.findById(creation.getName()).ifPresent(microservice -> {
            throw new MicroserviceAlreadyExistsException("microservice " + microservice.getName() + " already exists");
        });
    }

    private Microservice getMicroserviceForCreation(MicroserviceCreation creation, String terraformWorkspaceId) {
        Microservice microservice = new Microservice();
        microservice.setName(creation.getName());
        microservice.setTeam(creation.getTeam());
        microservice.setCreatedBy(creation.getCreatedBy());
        microservice.setGitUrl(GITHUB_URL + gitHubConfig.getGithubOwner() + "/" + creation.getName());
        microservice.setInfraGitUrl(GITHUB_URL + gitHubConfig.getGithubOwner() + "/" + creation.getName() + "-infra");
        microservice.setDeploymentGitUrl(GITHUB_URL + gitHubConfig.getGithubOwner() + "/" + creation.getName() + "-deployment");
        microservice.setTemplate(creation.getTemplate());
        microservice.setTerraformWorkspaceId(terraformWorkspaceId);

        return microservice;
    }
}
