package com.infra.resources.adapter.gateway.deployment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Deployment {
    String serviceName;
    String environment;
    String version;
    boolean useDefaultAutoscalingValues;
}
