package com.infra.resources.core.usecase.secret;

import com.infra.resources.adapter.controller.secret.SecretUpdate;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSecret {

    private final GetMicroservice getMicroservice;
    private final GetSecret getSecret;
    private final SecretRepository repository;
    private final DeploymentGateway deploymentGateway;

    public Secret execute(String serviceName, String name, SecretUpdate secretUpdate) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Secret secret = getSecret.execute(microservice.getName(), name);
        return updateSecret(secret, secretUpdate);
    }

    private Secret updateSecret(Secret secret, SecretUpdate secretUpdate) {
        if (Objects.nonNull(secretUpdate.getValue())) {
            secret.setValue(secretUpdate.getValue());
        }

        if (Objects.nonNull(secretUpdate.getDescription())) {
            secret.setDescription(secretUpdate.getDescription());
        }

        return repository.save(secret);
    }
}
