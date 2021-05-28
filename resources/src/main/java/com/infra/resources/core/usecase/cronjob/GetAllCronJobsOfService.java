package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllCronJobsOfService {

    private final GetMicroservice getMicroservice;
    private final CronJobRepository repository;

    public List<CronJob> execute(String serviceName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        return repository.findAllByMicroserviceName(microservice.getName());
    }
}
