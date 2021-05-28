package com.infra.releases.adapter.gateway.microservices;

import com.infra.releases.core.domain.Microservice;
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
public class MicroservicesGateway {

    private static final String HOSTNAME = "http://localhost";

    private final RestTemplate restTemplate;

    @Value("${server.port}")
    private int appPort;

    public Microservice getMicroservice(String name) {
        HttpEntity<Microservice> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<Microservice> response = restTemplate.exchange(
            getURI("/backend/microservice/" + name, Collections.emptyMap()), HttpMethod.GET, entity, Microservice.class);
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
