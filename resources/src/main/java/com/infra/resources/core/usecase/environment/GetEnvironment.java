package com.infra.resources.core.usecase.environment;

import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.exceptions.EnvironmentNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEnvironment {

    private final EnvironmentRepository environmentRepository;

    public Environment execute(String microserviceName, String envName) {
        Environment.EnvironmentId envId = new Environment.EnvironmentId(microserviceName, envName);
        return environmentRepository.findById(envId).orElseThrow(EnvironmentNotFoundException::new);
    }

    public Optional<Environment> safeExecute(String microserviceName, String envName) {
        Environment.EnvironmentId envId = new Environment.EnvironmentId(microserviceName, envName);
        return environmentRepository.findById(envId);
    }
}
