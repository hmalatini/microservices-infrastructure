package com.infra.releases.adapter.gateway.dockerhubregistry;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
class DockerHubRegistryClient {

    private static final String AUTH_HOSTNAME = "https://auth.docker.io/";
    private static final String REGISTRY_SERVICE_NAME = "registry.docker.io";
    private static final String REGISTRY_HOSTNAME = "https://registry.hub.docker.com/v2/";

    private final RestTemplate restTemplate;

    String getToken(String user, String repo) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        Map<String, String> queryParams = getAuthQueryParameters(user, repo);
        ResponseEntity<DockerRegistryTokenResponseDTO> tokenResponse = restTemplate.exchange(
            getURI(AUTH_HOSTNAME + "token", queryParams), HttpMethod.GET, entity,
            DockerRegistryTokenResponseDTO.class);
        return Objects.requireNonNull(tokenResponse.getBody())
                      .getToken();
    }

    List<String> getImages(String user, String repo, String registryToken) {
        HttpEntity<String> entity = new HttpEntity<>(getRegistryAuthHeaders(registryToken));
        ResponseEntity<DockerRegistryTagsResponseDTO> tagsResponse = restTemplate.exchange(
            getURI(REGISTRY_HOSTNAME + user + "/" + repo + "/tags/list", Collections.emptyMap()), HttpMethod.GET,
            entity,
            DockerRegistryTagsResponseDTO.class);
        return Objects.requireNonNull(tagsResponse.getBody())
                      .getTags();
    }

    private Map<String, String> getAuthQueryParameters(String user, String repo) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("service", REGISTRY_SERVICE_NAME);
        queryParams.put("scope", getDockerRegistryScope(user, repo));
        return queryParams;
    }

    private String getDockerRegistryScope(String user, String repo) {
        return "repository:" + user + "/" + repo + ":pull";
    }

    private URI getURI(String uri, Map<String, String> queryParams) {
        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(uri);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url = url.queryParam(entry.getKey(), entry.getValue());
        }

        return url.build()
                  .toUri();
    }

    private HttpHeaders getRegistryAuthHeaders(String registryToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + registryToken);
        return headers;
    }

}
