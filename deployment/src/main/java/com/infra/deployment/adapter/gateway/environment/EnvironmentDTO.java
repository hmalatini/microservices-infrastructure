package com.infra.deployment.adapter.gateway.environment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
class EnvironmentDTO {

    private String name;
    private boolean autoscalingEnabled;
    private int minReplicas;
    private int maxReplicas;
    private int autoscalingTargetCPU;
}
