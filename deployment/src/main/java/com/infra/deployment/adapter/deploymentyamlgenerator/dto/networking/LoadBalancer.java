package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoadBalancer {

    public enum LoadBalancerType {
        ROUND_ROBIN,
        LEAST_CONN,
        RANDOM,
        CONSISTENT_HASH
    }

    private LoadBalancerType type;
    private ConsistentHashConfig consistentHash;
}
