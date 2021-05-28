package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CircuitBreaker {

    private Integer consecutiveGatewayErrors;
    private Integer consecutive5xxErrors;
    private String interval;
    private String baseEjectionTime;
    private Integer maxEjectionPercent;
    private Integer minHealthPercent;
}
