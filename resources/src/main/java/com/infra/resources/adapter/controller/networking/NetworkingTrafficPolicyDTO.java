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
public class NetworkingTrafficPolicyDTO {

    private String name;
    private String environment;
    private LoadBalancerDTO loadBalancer;
    private CircuitBreakerDTO circuitBreaker;
}
