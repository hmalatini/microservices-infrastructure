package com.infra.resources.core.usecase.secret;

import com.infra.resources.adapter.repository.SecretRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllSecretsOfService {

    private final GetMicroservice getMicroservice;
    private final SecretRepository repository;

    public List<Secret> execute(String serviceName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        return repository.findAllByMicroserviceName(microservice.getName());
    }
}
