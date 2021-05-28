package com.infra.resources.core.usecase.microservice;

import com.infra.resources.adapter.repository.MicroserviceRepository;
import com.infra.resources.core.domain.Microservice;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllMicroservices {

    private final MicroserviceRepository microserviceRepository;

    public List<Microservice> execute() {
        List<Microservice> microserviceList = new ArrayList<>();
        microserviceRepository.findAll()
                              .forEach(microserviceList::add);
        return microserviceList;
    }
}
