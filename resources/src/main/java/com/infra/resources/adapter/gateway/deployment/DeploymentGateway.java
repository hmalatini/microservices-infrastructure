package com.infra.resources.adapter.gateway.deployment;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentCreation;
import com.infra.resources.adapter.gateway.deployment.client.DeploymentClient;
import com.infra.resources.adapter.gateway.deployment.dto.Deployment;
import com.infra.resources.adapter.gateway.deployment.mapper.DeploymentMapper;
import com.infra.resources.core.domain.Microservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeploymentGateway {

    private final DeploymentClient deploymentClient;

    public void createDeploymentFiles(Microservice microservice, EnvironmentCreation environment) {
        Deployment deployment = DeploymentMapper.getDeploymentFromMicroserviceAndEnvironment(microservice, environment);

        deploymentClient.createDeploymentFiles(deployment);
    }

    public void updateConfig(String microserviceName, String environmentName, String description) {
        deploymentClient.UpdateConfig(microserviceName, environmentName, description);
    }
}
