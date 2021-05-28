package com.infra.deployment.adapter.gateway.networking;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.Networking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkingGateway {

    private final NetworkingClient client;

    public Networking getNetworkingConfig(String serviceName, String envName) {
        NetworkingDTO dto = client.getNetworking(serviceName, envName);
        return NetworkingGatewayMapper.map(dto);
    }
}
