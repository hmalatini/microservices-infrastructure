package com.infra.resources.core.usecase.environment;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentUpdate;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.exceptions.UpdateEnvironmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEnvironment {

    private final GetEnvironment getEnvironment;
    private final EnvironmentRepository environmentRepository;
    private final DeploymentGateway deploymentGateway;

    public Environment execute(String microserviceName, String envName, EnvironmentUpdate environmentUpdate) {
        validate(environmentUpdate);
        Environment environment = getEnvironment.execute(microserviceName, envName);

        Environment updated = updateEnvironment(environmentUpdate, environment);
        deploymentGateway.updateConfig(microserviceName, environment.getName(), "Updated environment " + updated.getName());
        return updated;
    }

    private Environment updateEnvironment(EnvironmentUpdate environmentUpdate, Environment environment) {
        environment.setAutoscalingEnabled(environmentUpdate.getAutoscalingEnabled());
        environment.setMinReplicas(environmentUpdate.getMinReplicas());
        environment.setMaxReplicas(environmentUpdate.getMaxReplicas());
        environment.setAutoscalingTargetCPU(environmentUpdate.getAutoscalingTargetCPU());

        return environmentRepository.save(environment);
    }

    private void validate(EnvironmentUpdate environmentUpdate) {
        if (environmentUpdate.getAutoscalingEnabled() == null) {
            throw new UpdateEnvironmentException("Austoscaling does not contains value");
        }

        if (environmentUpdate.getMinReplicas() == null || environmentUpdate.getMinReplicas() < 0
            || environmentUpdate.getMinReplicas() > 100) {
            throw new UpdateEnvironmentException("Invalid min instances number");
        }

        if (environmentUpdate.getMaxReplicas() == null || environmentUpdate.getMaxReplicas() < 0
            || environmentUpdate.getMaxReplicas() > 100) {
            throw new UpdateEnvironmentException("Invalid max instances number");
        }

        if (environmentUpdate.getMinReplicas() > environmentUpdate.getMaxReplicas()) {
            throw new UpdateEnvironmentException("Min instances has to be lower than max instances");
        }

        if (environmentUpdate.getAutoscalingTargetCPU() == null || environmentUpdate.getAutoscalingTargetCPU() < 0
            || environmentUpdate.getAutoscalingTargetCPU() > 100) {
            throw new UpdateEnvironmentException("Invalid target cpu number");
        }
    }
}
