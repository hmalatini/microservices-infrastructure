package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Networking {
    private List<NetworkingTrafficPolicy> trafficPolicies;
    private NetworkingPublicConfig publicConfig;
    private NetworkingPrivateConfig privateConfig;
}
