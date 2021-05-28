package com.infra.releases.core.usecase;

import com.infra.releases.adapter.gateway.dockerhubregistry.DockerHubRegistryGateway;
import com.infra.releases.adapter.gateway.microservices.MicroservicesGateway;
import com.infra.releases.core.domain.Microservice;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllReleasesOfService {

    private final MicroservicesGateway microservicesGateway;
    private final DockerHubRegistryGateway dockerHubRegistryGateway;

    public List<String> execute(String microserviceName) {
        Microservice microservice = microservicesGateway.getMicroservice(microserviceName);
        return getServiceReleases(microservice);
    }

    private List<String> getServiceReleases(Microservice microservice) {
        return dockerHubRegistryGateway.getAllReleases()
                                       .stream()
                                       .filter(release -> belongToMicroservice(release, microservice.getName()))
                                       .map(release -> extractVersionFromTag(release, microservice.getName()))
                                       .collect(
                                           Collectors.toList());
    }

    private String extractVersionFromTag(String releaseName, String microserviceName) {
        return releaseName.replaceAll("service-name-" + microserviceName + "-version-", "");
    }

    private boolean belongToMicroservice(String releaseName, String microserviceName) {
        return releaseName.startsWith("service-name-" + microserviceName + "-version-");
    }
}
