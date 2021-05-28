package com.infra.deployment.core.usecase;

import com.infra.deployment.adapter.repository.DeploymentRepository;
import com.infra.deployment.core.domain.Deployment;
import com.infra.deployment.core.exceptions.VersionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateConfigWithDeployment {

    private final DeploymentExecutor deploymentExecutor;
    private final DeploymentRepository deploymentRepository;

    public void execute(String microserviceName, String environmentName, String msgDescription) {
        Deployment deployment = deploymentRepository.findByMicroserviceNameAndEnvironmentName(microserviceName,
            environmentName).orElseThrow(VersionNotFoundException::new);

        deploymentExecutor.execute(deployment, msgDescription);
    }
}
