package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CircuitBreakerDTO {

    private Integer consecutiveGatewayErrors;
    private Integer consecutive5xxErrors;
    private String interval;
    private String baseEjectionTime;
    private Integer maxEjectionPercent;
    private Integer minHealthPercent;
}
