package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EndpointDTO {

    private String name;
    private EndpointMatchDTO match;
    private String rewrite;
    private String redirect;
    private String timeout;
    private RetryConfigDTO retries;
    private FaultConfigDTO faults;
    private HeadersConfigDTO headers;
    private NetworkingTrafficPolicyDTO trafficPolicy;
}
