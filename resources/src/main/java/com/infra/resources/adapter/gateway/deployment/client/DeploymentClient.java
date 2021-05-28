package com.infra.resources.adapter.gateway.deployment.client;

import com.infra.resources.adapter.gateway.deployment.dto.Deployment;
import com.infra.resources.adapter.gateway.deployment.dto.UpdateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DeploymentClient {

    private final RestTemplate restTemplate;
    private final String appHost;

    public DeploymentClient(@Value("${server.port}") int appPort) {
        this.restTemplate = new RestTemplate();
        this.appHost = "http://localhost:" + appPort;
    }

    public void createDeploymentFiles(Deployment deployment) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(appHost + "/backend/deployment");
        HttpEntity<Deployment> entity = new HttpEntity<>(deployment);
        restTemplate.exchange(uri.build()
                                 .toUri(), HttpMethod.POST, entity, Deployment.class);
    }

    public void UpdateConfig(String microserviceName, String environmentName, String description) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(appHost + "/backend/deployment")
                                                       .queryParam("microserviceName", microserviceName)
                                                       .queryParam("environmentName", environmentName);
        HttpEntity<UpdateConfig> entity = new HttpEntity<>(new UpdateConfig(description));
        restTemplate.exchange(uri.build()
                                 .toUri(), HttpMethod.PUT, entity, String.class);
    }
}
