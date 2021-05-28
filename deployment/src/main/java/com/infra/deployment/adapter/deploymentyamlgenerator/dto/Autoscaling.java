package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Autoscaling {
    private boolean enabled;
    private int minReplicas;
    private int maxReplicas;
    private int targetCPUUtilizationPercentage;
}
