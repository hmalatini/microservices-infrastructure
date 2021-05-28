package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.EndpointDTO;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.NetworkingEndpointRepository;
import com.infra.resources.adapter.repository.NetworkingTrafficPolicyRepository;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.exceptions.EndpointNotFoundException;
import com.infra.resources.core.exceptions.TrafficPolicyNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEndpoint {

    private final NetworkingEndpointRepository repository;
    private final NetworkingTrafficPolicyRepository trafficPolicyRepository;
    private final EndpointDTOtoNetworkEndpoint mapper;
    private final DeploymentGateway deploymentGateway;

    public NetworkingEndpoint execute(String microserviceName, String environmentName, String endpointName,
                                      Type endpointType, String trafficPolicyName,
                                      EndpointDTO endpointDTO) {
        NetworkingEndpoint currentEndpoint = getEndpoint(microserviceName, environmentName, endpointName, endpointType);
        NetworkingTrafficPolicy trafficPolicy = getTrafficPolicy(microserviceName, environmentName, trafficPolicyName);

        NetworkingEndpoint updated = mapper.map(microserviceName, environmentName, endpointName, endpointType,
            endpointDTO, trafficPolicy, currentEndpoint.getId());

        mapUpdateListRelations(currentEndpoint, updated);
        repository.save(updated);
        deploymentGateway.updateConfig(microserviceName, environmentName, "Updated endpoint " + updated.getName());
        return updated;
    }

    private void mapUpdateListRelations(NetworkingEndpoint current, NetworkingEndpoint update) {
        if (Objects.nonNull(current.getHeaders())) {
            current.getHeaders()
                   .clear();
            if (Objects.nonNull(update.getHeaders())) {
                current.getHeaders()
                       .addAll(update.getHeaders());
            }
            update.setHeaders(current.getHeaders());
        }
        if (Objects.nonNull(current.getNoHeaders())) {
            current.getNoHeaders()
                   .clear();
            if (Objects.nonNull(update.getNoHeaders())) {
                current.getNoHeaders()
                       .addAll(update.getNoHeaders());
            }
            update.setNoHeaders(current.getNoHeaders());
        }
        if (Objects.nonNull(current.getQueryParams())) {
            current.getQueryParams()
                   .clear();
            if (Objects.nonNull(update.getQueryParams())) {
                current.getQueryParams()
                       .addAll(update.getQueryParams());
            }
            update.setQueryParams(current.getQueryParams());
        }
        if (Objects.nonNull(current.getHeaderConfigRequests())) {
            current.getHeaderConfigRequests()
                   .clear();
            if (Objects.nonNull(update.getHeaderConfigRequests())) {
                current.getHeaderConfigRequests()
                       .addAll(update.getHeaderConfigRequests());
            }
            update.setHeaderConfigRequests(current.getHeaderConfigRequests());
        }
        if (Objects.nonNull(current.getHeaderConfigResponses())) {
            current.getHeaderConfigResponses()
                   .clear();
            if (Objects.nonNull(update.getHeaderConfigResponses())) {
                current.getHeaderConfigResponses()
                       .addAll(update.getHeaderConfigResponses());
            }
            update.setHeaderConfigResponses(current.getHeaderConfigResponses());
        }
    }

    private NetworkingEndpoint getEndpoint(String microserviceName, String environmentName, String endpointName,
                                           Type endpointType) {
        return repository.findByMicroserviceNameAndEnvironmentNameAndEndpointTypeAndName(microserviceName,
            environmentName, endpointType, endpointName)
                         .orElseThrow(EndpointNotFoundException::new);
    }

    private NetworkingTrafficPolicy getTrafficPolicy(String microserviceName, String environmentName,
                                                     String trafficPolicyName) {
        if (Strings.isBlank(trafficPolicyName)) {
            return null;
        }
        return trafficPolicyRepository.findByMicroserviceNameAndEnvironmentNameAndName(microserviceName,
            environmentName, trafficPolicyName)
                                      .orElseThrow(
                                          TrafficPolicyNotFoundException::new);
    }
}
