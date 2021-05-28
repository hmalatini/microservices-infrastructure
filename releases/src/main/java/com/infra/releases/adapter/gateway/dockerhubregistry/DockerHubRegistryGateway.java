package com.infra.releases.adapter.gateway.dockerhubregistry;

import com.infra.releases.config.ReleaseRegistryConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DockerHubRegistryGateway {

    private final DockerHubRegistryClient dockerHubRegistryClient;
    private final ReleaseRegistryConfig dockerRegistryConfig;

    public List<String> getAllReleases() {
        String registryToken = dockerHubRegistryClient.getToken(dockerRegistryConfig.getRegistryUser(),
            dockerRegistryConfig.getRegistryRepo());
        return dockerHubRegistryClient.getImages(dockerRegistryConfig.getRegistryUser(),
            dockerRegistryConfig.getRegistryRepo(), registryToken);
    }
}
