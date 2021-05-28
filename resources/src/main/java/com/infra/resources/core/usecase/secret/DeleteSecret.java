package com.infra.resources.core.usecase.secret;

import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.domain.Secret.SecretId;
import com.infra.resources.core.exceptions.SecretNotFoundException;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSecret {

    private final GetMicroservice getMicroservice;
    private final SecretRepository repository;
    private final DeploymentGateway deploymentGateway;

    public Secret execute(String serviceName, String name) {
        Microservice microservice = getMicroservice.execute(serviceName);
        return deleteSecret(name, microservice);
    }

    private Secret deleteSecret(String name, Microservice microservice) {
        SecretId secretId = new SecretId(microservice.getName(), name);
        Secret secret = repository.findById(secretId).orElseThrow(SecretNotFoundException::new);
        repository.delete(secret);

        return secret;
    }
}
