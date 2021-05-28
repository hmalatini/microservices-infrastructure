package com.infra.resources.adapter.gateway.deployment.mapper;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentCreation;
import com.infra.resources.adapter.gateway.deployment.dto.Deployment;
import com.infra.resources.core.domain.Microservice;

public class DeploymentMapper {
    public static Deployment getDeploymentFromMicroserviceAndEnvironment(Microservice microservice, EnvironmentCreation environment) {
        return Deployment.builder()
                .serviceName(microservice.getName())
                .environment(environment.getEnvironmentName())
                .version(environment.getImageVersion())
                .useDefaultAutoscalingValues(true)
                .build();
    }
}
