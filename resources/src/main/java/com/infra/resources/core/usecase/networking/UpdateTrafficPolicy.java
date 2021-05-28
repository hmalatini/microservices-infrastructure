package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.NetworkingTrafficPolicyDTO;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.exceptions.TrafficPolicyNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTrafficPolicy {

    private final NetworkingTrafficPolicyRepository repository;
    private final TrafficPolictyDTOtoNetworkTrafficPolicy mapper;
    private final DeploymentGateway deploymentGateway;

    public NetworkingTrafficPolicy execute(String microserviceName, String environmentName, String name,
                                           NetworkingTrafficPolicyDTO config) {
        if (Objects.isNull(config)) {
            return null;
        }

        NetworkingTrafficPolicy current = getTrafficPolicy(microserviceName, environmentName, name);

        NetworkingTrafficPolicy update = mapper.map(microserviceName, environmentName, name, config, current.getId());

        repository.save(update);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Updated traffic policy " + update.getName());
        return update;
    }

    private NetworkingTrafficPolicy getTrafficPolicy(String microserviceName, String environmentName, String name) {
        return repository.findByMicroserviceNameAndEnvironmentNameAndName(microserviceName, environmentName, name)
                         .orElseThrow(
                             TrafficPolicyNotFoundException::new);
    }
}
