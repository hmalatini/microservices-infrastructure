package com.infra.deployment.adapter.gateway.secrets;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecretsGateway {

    private final SecretsClient client;

    public Map<String, String> getSecretsOfService(String serviceName) {
        return client.getSecrets(serviceName)
                     .stream()
                     .collect(Collectors.toMap(
                         SecretsDTO::getName,
                         SecretsDTO::getValue
                     ));
    }
}
