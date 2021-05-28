package com.infra.deployment.adapter.gateway.microservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MicroserviceDTO {

    private String name;
    private String deploymentGitUrl;
}
