package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.NetworkingTrafficPolicyDTO;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTrafficPolicy {

    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final NetworkingTrafficPolicyRepository repository;
    private final TrafficPolictyDTOtoNetworkTrafficPolicy mapper;
    private final DeploymentGateway deploymentGateway;

    public NetworkingTrafficPolicy execute(String microserviceName, String environmentName, String name,
                                           NetworkingTrafficPolicyDTO config) {
        if (Objects.isNull(config)) {
            return null;
        }

        Microservice microservice = getMicroservice.execute(microserviceName);
        Environment environment = getEnvironment.execute(microservice.getName(), environmentName);

        NetworkingTrafficPolicy trafficPolicy = mapper.map(microserviceName, environmentName, name, config, null);

        repository.save(trafficPolicy);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Created traffic policy " + trafficPolicy.getName());
        return trafficPolicy;
    }
}
