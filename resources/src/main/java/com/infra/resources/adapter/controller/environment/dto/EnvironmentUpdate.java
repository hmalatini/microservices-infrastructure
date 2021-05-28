package com.infra.resources.adapter.controller.environment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class EnvironmentUpdate {
    private Boolean autoscalingEnabled;
    private Integer autoscalingTargetCPU;
    private Integer minReplicas;
    private Integer maxReplicas;
}
