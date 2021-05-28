package com.infra.resources.core.usecase.environment;

import com.infra.resources.adapter.infraresources.destroy.ResourceDestroyerManager;
import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.usecase.cronjob.DeleteCronJob;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import com.infra.resources.core.usecase.networking.DeleteEndpoint;
import com.infra.resources.core.usecase.networking.DeleteTrafficPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DestroyEnvironment {

    private final ResourceDestroyerManager resourceDestroyerManager;
    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final EnvironmentRepository environmentRepository;
    private final DeleteCronJob deleteCronJob;
    private final DeleteEndpoint deleteEndpoint;
    private final DeleteTrafficPolicy deleteTrafficPolicy;

    public Environment execute(String microserviceName, String envName) {
        Microservice microservice = getMicroservice.execute(microserviceName);
        Environment environment = getEnvironment.execute(microserviceName, envName);

        resourceDestroyerManager.destroyResource(
            microservice.getName(), environment.getName() + "-environment", environment.getTerraformWorkspaceId(),
            microservice.getName() + "-env-" + environment.getName(), microservice.getName() + "-infra",
            "/environment/" + environment.getName());
        environmentRepository.delete(environment);
        cleanEnvironmentResources(microserviceName, envName);
        return environment;
    }

    private void cleanEnvironmentResources(String microserviceName, String envName) {
        deleteCronJob.executeAll(microserviceName, envName);
        deleteEndpoint.executeAll(microserviceName, envName);
        deleteTrafficPolicy.executeAll(microserviceName, envName);
    }
}
