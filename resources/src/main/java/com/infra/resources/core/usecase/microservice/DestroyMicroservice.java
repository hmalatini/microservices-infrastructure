package com.infra.resources.core.usecase.microservice;

import com.infra.resources.adapter.gateway.github.GitHubGateway;
import com.infra.resources.adapter.infraresources.destroy.ResourceDestroyerManager;
import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.adapter.repository.MicroserviceRepository;
import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.NoEmptyResourcesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyMicroservice {

    private final ResourceDestroyerManager resourceDestroyerManager;
    private final GetMicroservice getMicroservice;
    private final GitHubGateway gitHubGateway;
    private final EnvironmentRepository environmentRepository;
    private final SecretRepository secretRepository;
    private final MicroserviceRepository repository;

    public Microservice execute(String microserviceName) {
        Microservice microservice = getMicroservice.execute(microserviceName);

        checkExistingResources(microserviceName);
        secretRepository.deleteAll();

        resourceDestroyerManager.destroyResource(
                microserviceName, microserviceName, microservice.getTerraformWorkspaceId(),
                microserviceName + "-service", microserviceName + "-infra", "/service");
        gitHubGateway.deleteRepo(microserviceName + "-infra");
        repository.delete(microservice);
        return microservice;
    }

    private void checkExistingResources(String microserviceName) {
        if (!environmentRepository.findAllByMicroserviceName(microserviceName).isEmpty())
            throw new NoEmptyResourcesException("microservices still have environments");
    }
}
