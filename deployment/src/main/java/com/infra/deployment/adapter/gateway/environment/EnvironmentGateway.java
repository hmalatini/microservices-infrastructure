package com.infra.deployment.adapter.gateway.environment;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.Autoscaling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvironmentGateway {

    private final EnvironmentClient client;

    public Autoscaling getAutoscalingValues(String serviceName, String environmentName) {
        EnvironmentDTO environmentDTO = client.getEnvironment(serviceName, environmentName);
        return map(environmentDTO);
    }

    private Autoscaling map(EnvironmentDTO environmentDTO) {
        return Autoscaling.builder()
                          .enabled(environmentDTO.isAutoscalingEnabled())
                          .minReplicas(environmentDTO.getMinReplicas())
                          .maxReplicas(environmentDTO.getMaxReplicas())
                          .targetCPUUtilizationPercentage(environmentDTO.getAutoscalingTargetCPU())
                          .build();
    }
}
