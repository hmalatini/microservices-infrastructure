package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Endpoint {

    private String name;
    private EndpointMatch match;
    private String rewrite;
    private String redirect;
    private String timeout;
    private RetryConfig retries;
    private FaultConfig faults;
    private HeadersConfig headers;
    private String trafficPolicyName;
}
