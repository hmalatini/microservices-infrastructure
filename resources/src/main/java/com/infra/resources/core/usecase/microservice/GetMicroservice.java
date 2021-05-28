package com.infra.resources.core.usecase.microservice;

import com.infra.resources.adapter.repository.MicroserviceRepository;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.MicroserviceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMicroservice {

    private final MicroserviceRepository microserviceRepository;

    public Microservice execute(String microserviceName) {
        return microserviceRepository.findById(microserviceName).orElseThrow(MicroserviceNotFoundException::new);
    }
}
