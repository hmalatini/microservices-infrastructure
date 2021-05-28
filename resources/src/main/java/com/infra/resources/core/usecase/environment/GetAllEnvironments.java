package com.infra.resources.core.usecase.environment;

import com.infra.resources.adapter.repository.EnvironmentRepository;
import com.infra.resources.core.domain.Environment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllEnvironments {

    private final EnvironmentRepository environmentRepository;

    public List<Environment> execute(String microserviceName) {
        return environmentRepository.findAllByMicroserviceName(microserviceName);
    }
}
