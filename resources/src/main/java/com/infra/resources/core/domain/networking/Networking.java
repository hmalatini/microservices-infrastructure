package com.infra.resources.core.domain.networking;

import java.util.List;
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
public class Networking {

    private List<NetworkingTrafficPolicy> trafficPolicies;
    private List<NetworkingEndpoint> publicEndpoints;
    private List<NetworkingEndpoint> privateEndpoints;
}
