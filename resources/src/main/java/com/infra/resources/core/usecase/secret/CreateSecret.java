package com.infra.resources.core.usecase.secret;

import com.infra.resources.adapter.controller.secret.SecretCreation;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.exceptions.SecretAlreadyExistsException;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSecret {

    private final GetMicroservice getMicroservice;
    private final GetSecret getSecret;
    private final SecretRepository repository;
    private final DeploymentGateway deploymentGateway;

    public Secret execute(SecretCreation creation, String microserviceName) {
        Microservice microservice = getMicroservice.execute(microserviceName);

        checkIfExists(microserviceName, creation);

        Secret secret = getSecretFromCreation(creation, microservice);
        repository.save(secret);
        return secret;
    }

    private void checkIfExists(String microserviceName, SecretCreation creation) {
        getSecret.safeExecute(microserviceName, creation.getName())
                 .ifPresent(secret -> {
                         throw new SecretAlreadyExistsException("secret " + secret.getName() + " already exists");
                     }
                 );
    }

    private Secret getSecretFromCreation(SecretCreation creation, Microservice microservice) {
        Secret secret = new Secret();
        secret.setName(creation.getName());
        secret.setValue(creation.getValue());
        secret.setDescription(creation.getDescription());
        secret.setSensitive(creation.isSensitive());
        secret.setCreatedBy(creation.getCreatedBy());
        secret.setMicroserviceName(microservice.getName());

        return secret;
    }
}
