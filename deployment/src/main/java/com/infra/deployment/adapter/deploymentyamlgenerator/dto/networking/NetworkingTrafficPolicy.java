package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NetworkingTrafficPolicy {
    private String name;
    private LoadBalancer loadBalancer;
    private CircuitBreaker circuitBreaker;
}
