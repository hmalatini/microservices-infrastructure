package com.infra.resources.adapter.controller.networking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CircuitBreakerDTO {

    private Integer consecutiveGatewayErrors;
    private Integer consecutive5xxErrors;
    private String interval;
    private String baseEjectionTime;
    private Integer maxEjectionPercent;
    private Integer minHealthPercent;
}
