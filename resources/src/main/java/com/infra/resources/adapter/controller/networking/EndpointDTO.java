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
public class EndpointDTO {

    private String name;
    private String environment;
    private EndpointMatchDTO match;
    private String rewrite;
    private String redirect;
    private String timeout;
    private RetryConfigDTO retries;
    private FaultConfigDTO faults;
    private HeadersConfigDTO headers;
    private NetworkingTrafficPolicyDTO trafficPolicy;
}
