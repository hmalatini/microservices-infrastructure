package com.infra.resources.core.usecase.environment;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentCreation;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.infraresources.creation.CreationResourceManager;
import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.config.RegistryConfig;
import com.infra.resources.core.domain.Cluster;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.EnvironmentAlreadyExistsException;
import com.infra.resources.core.usecase.cluster.GetCluster;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEnvironment {

    private final DeploymentGateway deploymentGateway;
    private final GetEnvironment getEnvironment;
    private final GetMicroservice getMicroservice;
    private final GetCluster getCluster;
    private final EnvironmentRepository environmentRepository;
    private final CreationResourceManager creationResourceManager;

    public Environment execute(EnvironmentCreation creation, String microserviceName) {
        Microservice microservice = getMicroservice.execute(microserviceName);
        Cluster cluster = getCluster.execute(creation.getClusterName());

        checkIfExists(microserviceName, creation);

        deploymentGateway.createDeploymentFiles(microservice, creation);
        String workspaceId = creationResourceManager.createEnvironment(creation, microservice, cluster);
        return environmentRepository.save(getEnvironmentFromCreation(creation, microservice, workspaceId));
    }

    private void checkIfExists(String microserviceName, EnvironmentCreation creation) {
        getEnvironment.safeExecute(microserviceName, creation.getEnvironmentName()).ifPresent(env -> {
            throw new EnvironmentAlreadyExistsException("environment " + env.getName() + " already exist");
        });
    }

    private Environment getEnvironmentFromCreation(EnvironmentCreation creation, Microservice microservice, String terraformWorkspaceId) {
        Environment environment = new Environment();

        environment.setName(creation.getEnvironmentName());
        environment.setMicroserviceName(microservice.getName());
        environment.setClusterName(creation.getClusterName());
        environment.setTest(creation.getIsTest());
        environment.setTerraformWorkspaceId(terraformWorkspaceId);
        environment.setCreatedBy(creation.getCreatedBy());
        environment.setAutoscalingEnabled(true);
        environment.setAutoscalingTargetCPU(80);
        environment.setMinReplicas(1);
        environment.setMaxReplicas(5);

        return environment;
    }
}
