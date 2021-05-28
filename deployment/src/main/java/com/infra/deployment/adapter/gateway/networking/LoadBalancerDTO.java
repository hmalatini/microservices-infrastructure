package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoadBalancerDTO {

    public enum LoadBalancerType {
        ROUND_ROBIN,
        LEAST_CONN,
        RANDOM,
        CONSISTENT_HASH
    }

    private LoadBalancerType type;
    private ConsistentHashConfigDTO consistentHash;
}
