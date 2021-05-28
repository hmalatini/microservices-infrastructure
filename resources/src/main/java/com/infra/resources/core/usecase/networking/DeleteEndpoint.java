package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingEndpointRepository;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.exceptions.EndpointNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEndpoint {

    private final NetworkingEndpointRepository repository;
    private final DeploymentGateway deploymentGateway;

    public NetworkingEndpoint execute(String microserviceName, String environmentName, String endpointName,
                                      Type endpointType) {

        NetworkingEndpoint endpoint = getEndpoint(microserviceName, environmentName, endpointName, endpointType);

        repository.delete(endpoint);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Deleted endpoint " + endpoint.getName());
        return endpoint;
    }

    public List<NetworkingEndpoint> executeAll(String microserviceName, String environmentName) {
        List<NetworkingEndpoint> endpointList = repository.findAllByMicroserviceNameAndEnvironmentName(microserviceName,
            environmentName);
        repository.deleteAll(endpointList);
        return endpointList;
    }

    private NetworkingEndpoint getEndpoint(String microserviceName, String environmentName, String endpointName,
                                           Type endpointType) {
        return repository.findByMicroserviceNameAndEnvironmentNameAndEndpointTypeAndName(microserviceName,
            environmentName, endpointType, endpointName)
                         .orElseThrow(EndpointNotFoundException::new);
    }
}
