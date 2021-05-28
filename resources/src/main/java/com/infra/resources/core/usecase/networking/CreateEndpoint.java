package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.EndpointDTO;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingEndpointRepository;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.exceptions.TrafficPolicyNotFoundException;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEndpoint {

    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final NetworkingEndpointRepository repository;
    private final NetworkingTrafficPolicyRepository trafficPolicyRepository;
    private final EndpointDTOtoNetworkEndpoint mapper;
    private final DeploymentGateway deploymentGateway;

    public NetworkingEndpoint execute(String microserviceName, String environmentName, String endpointName,
                                      Type endpointType, String trafficPolicyName,
                                      EndpointDTO endpointDTO) {
        if (Objects.isNull(endpointDTO)) {
            return null;
        }
        Microservice microservice = getMicroservice.execute(microserviceName);
        Environment environment = getEnvironment.execute(microservice.getName(), environmentName);

        NetworkingTrafficPolicy trafficPolicy = getTrafficPolicy(microserviceName, environmentName, trafficPolicyName);

        NetworkingEndpoint endpoint = mapper.map(microserviceName, environmentName, endpointName, endpointType,
            endpointDTO, trafficPolicy, null);

        repository.save(endpoint);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Created endpoint " + endpoint.getName());
        return endpoint;
    }

    private NetworkingTrafficPolicy getTrafficPolicy(String microserviceName, String environmentName,
                                                     String trafficPolicyName) {
        if (Objects.isNull(trafficPolicyName)) {
            return null;
        }
        return trafficPolicyRepository.findByMicroserviceNameAndEnvironmentNameAndName(microserviceName,
            environmentName, trafficPolicyName)
                                      .orElseThrow(
                                          TrafficPolicyNotFoundException::new);
    }
}
