package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.domain.CronJob.CronJobId;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.CronJobNotFoundException;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCronJob {

    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final CronJobRepository repository;

    public CronJob execute(String serviceName, String envName, String cronJobName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Environment environment = getEnvironment.execute(microservice.getName(), envName);
        CronJobId id = new CronJobId(microservice.getName(), environment.getName(), cronJobName);
        return repository.findById(id)
                         .orElseThrow(CronJobNotFoundException::new);
    }

    public Optional<CronJob> safeExecute(String serviceName, String envName, String cronJobName) {
        Microservice microservice = getMicroservice.execute(serviceName);
        Environment environment = getEnvironment.execute(microservice.getName(), envName);
        CronJobId id = new CronJobId(microservice.getName(), environment.getName(), cronJobName);
        return repository.findById(id);
    }
}
