package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllCronJobsOfServiceAndEnvironment {

    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final CronJobRepository repository;

    public List<CronJob> execute(String serviceName, String envName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Optional<Environment> environment = getEnvironment.safeExecute(microservice.getName(), envName);
        if (environment.isEmpty()) {
            return Collections.emptyList();
        }

        return repository.findAllByMicroserviceNameAndEnvironmentName(microservice.getName(), environment.get().getName());
    }
}
