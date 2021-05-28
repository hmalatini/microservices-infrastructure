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
