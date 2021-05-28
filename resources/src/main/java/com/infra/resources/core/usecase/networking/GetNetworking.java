package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.repository.NetworkingEndpointRepository;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.networking.Networking;
import com.infra.resources.core.domain.networking.Networking.NetworkingBuilder;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNetworking {

    private final NetworkingTrafficPolicyRepository trafficPolicyRepository;
    private final NetworkingEndpointRepository endpointRepository;

    public Networking execute(String microserviceName, String environmentName) {
        NetworkingBuilder builder = Networking.builder();

        getTrafficPolicies(microserviceName, environmentName, builder);
        getEndpoints(microserviceName, environmentName, builder);

        return builder.build();
    }

    public Networking execute(String microserviceName) {
        NetworkingBuilder builder = Networking.builder();

        getTrafficPolicies(microserviceName, null, builder);
        getEndpoints(microserviceName, null, builder);

        return builder.build();
    }

    private void getEndpoints(String microserviceName, String environmentName, NetworkingBuilder builder) {
        List<NetworkingEndpoint> endpoints;
        if (Objects.isNull(environmentName)) {
            endpoints = endpointRepository.findAllByMicroserviceName(microserviceName);
        } else {
            endpoints = endpointRepository.findAllByMicroserviceNameAndEnvironmentName(microserviceName,
                environmentName);
        }

        Map<Type, List<NetworkingEndpoint>> endpointMap = endpoints.stream()
                                                                   .collect(Collectors.groupingBy(
                                                                       NetworkingEndpoint::getEndpointType));

        builder.privateEndpoints(endpointMap.getOrDefault(Type.PRIVATE, Collections.emptyList()));
        builder.publicEndpoints(endpointMap.getOrDefault(Type.PUBLIC, Collections.emptyList()));
    }

    private void getTrafficPolicies(String microserviceName, String environmentName, NetworkingBuilder builder) {
        List<NetworkingTrafficPolicy> trafficPolicies;
        if (Objects.isNull(environmentName)) {
            trafficPolicies = trafficPolicyRepository.findAllByMicroserviceName(microserviceName);
        } else {
            trafficPolicies = trafficPolicyRepository.findAllByMicroserviceNameAndEnvironmentName(
                microserviceName, environmentName);
        }

        builder.trafficPolicies(trafficPolicies);
    }

}
