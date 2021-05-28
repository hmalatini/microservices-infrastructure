package com.infra.resources.adapter.controller.networking;

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
public class NetworkingDTO {

    private List<NetworkingTrafficPolicyDTO> trafficPolicies;
    private List<EndpointDTO> publicEndpoints;
    private List<EndpointDTO> privateEndpoints;
}
