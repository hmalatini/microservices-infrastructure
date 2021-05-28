package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.controller.cronjob.CronJobCreation;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.exceptions.SecretAlreadyExistsException;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCronJob {

    private final GetMicroservice getMicroservice;
    private final GetEnvironment getEnvironment;
    private final GetCronJob getCronJob;
    private final CronJobRepository repository;
    private final DeploymentGateway deploymentGateway;

    public CronJob execute(CronJobCreation creation, String microserviceName, String environmentName) {
        Microservice microservice = getMicroservice.execute(microserviceName);
        Environment environment = getEnvironment.execute(microservice.getName(), environmentName);

        checkIfExists(microserviceName, environmentName, creation);

        CronJob cronjob = getCronJobFromCreation(creation, microservice, environment);
        repository.save(cronjob);
        deploymentGateway.updateConfig(microservice.getName(), environment.getName(), "Created cron job " + creation.getName());
        return cronjob;
    }

    private void checkIfExists(String microserviceName, String environmentName, CronJobCreation creation) {
        getCronJob.safeExecute(microserviceName, environmentName, creation.getName())
                  .ifPresent(cronJob -> {
                          throw new SecretAlreadyExistsException("cronjob " + cronJob.getName() + " already exists");
                      }
                  );
    }

    private CronJob getCronJobFromCreation(CronJobCreation creation, Microservice microservice,
                                           Environment environment) {
        CronJob cronJob = new CronJob();
        cronJob.setMicroserviceName(microservice.getName());
        cronJob.setEnvironmentName(environment.getName());
        cronJob.setName(creation.getName());
        cronJob.setSchedule(creation.getSchedule());
        cronJob.setEndpoint(creation.getEndpoint());

        return cronJob;
    }
}
