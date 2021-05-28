package com.infra.resources.core.usecase.secret;

import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.domain.Secret.SecretId;
import com.infra.resources.core.exceptions.SecretNotFoundException;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSecret {

    private final GetMicroservice getMicroservice;
    private final SecretRepository repository;

    public Secret execute(String serviceName, String secretName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Secret.SecretId id = getSecretId(microservice.getName(), secretName);
        return repository.findById(id)
                         .orElseThrow(SecretNotFoundException::new);
    }

    public Optional<Secret> safeExecute(String serviceName, String secretName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Secret.SecretId id = getSecretId(microservice.getName(), secretName);
        return repository.findById(id);
    }

    private Secret.SecretId getSecretId(String serviceName, String secretName) {
        Secret.SecretId id = new SecretId();
        id.setMicroserviceName(serviceName);
        id.setName(secretName);

        return id;
    }
}
