package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.exceptions.DestroyResourceException;
import com.infra.resources.core.exceptions.TrafficPolicyNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTrafficPolicy {

    private final NetworkingTrafficPolicyRepository repository;
    private final DeploymentGateway deploymentGateway;

    public NetworkingTrafficPolicy execute(String microserviceName, String environmentName, String name) {
        NetworkingTrafficPolicy trafficPolicy = getTrafficPolicy(microserviceName, environmentName, name);
        if (Objects.nonNull(trafficPolicy.getEndpoints()) && !trafficPolicy.getEndpoints().isEmpty()) {
            throw new DestroyResourceException("there are endpoints using this traffic policy");
        }

        repository.delete(trafficPolicy);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Deleted traffic policy " + trafficPolicy.getName());
        return trafficPolicy;
    }

    public List<NetworkingTrafficPolicy> executeAll(String microserviceName, String environmentName) {
        List<NetworkingTrafficPolicy> trafficPolicyList = repository.findAllByMicroserviceNameAndEnvironmentName(microserviceName,
            environmentName);
        repository.deleteAll(trafficPolicyList);
        return trafficPolicyList;
    }

    private NetworkingTrafficPolicy getTrafficPolicy(String microserviceName, String environmentName, String name) {
        return repository.findByMicroserviceNameAndEnvironmentNameAndName(microserviceName, environmentName, name)
                         .orElseThrow(
                             TrafficPolicyNotFoundException::new);
    }
}
