package com.infra.deployment.core.usecase;

import com.infra.deployment.adapter.repository.DeploymentRepository;
import com.infra.deployment.core.domain.Deployment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDeployment {

    private final DeploymentExecutor executor;
    private final DeploymentRepository deploymentRepository;


    public void execute(Deployment deployment, String description) {
        executor.execute(deployment, description);
        deploymentRepository.save(deployment);
    }
}
