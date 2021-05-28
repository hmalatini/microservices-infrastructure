package com.infra.deployment.adapter.gateway.environment;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
class EnvironmentClient {

    private static final String HOSTNAME = "http://localhost";

    private final RestTemplate restTemplate;

    @Value("${server.port}")
    private int appPort;

    EnvironmentDTO getEnvironment(String serviceName, String environmentName) {
        HttpEntity<EnvironmentDTO> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<EnvironmentDTO> response = restTemplate.exchange(
            getURI("/backend/" + serviceName + "/environment/" + environmentName, Collections.emptyMap()), HttpMethod.GET,
            entity, EnvironmentDTO.class);
        return response.getBody();
    }

    private URI getURI(String uri, Map<String, String> queryParams) {
        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(HOSTNAME + ":" + appPort + uri);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url = url.queryParam(entry.getKey(), entry.getValue());
        }

        return url.build()
                  .toUri();
    }

    private HttpHeaders getHeaders() {
        return new HttpHeaders();
    }
}
