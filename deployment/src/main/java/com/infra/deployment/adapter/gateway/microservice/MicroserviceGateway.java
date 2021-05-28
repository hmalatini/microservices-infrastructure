package com.infra.deployment.adapter.gateway.microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MicroserviceGateway {

    private final MicroserviceClient client;

    public MicroserviceDTO getMicroservice(String serviceName) {
        return client.getMicroservice(serviceName);
    }

}
