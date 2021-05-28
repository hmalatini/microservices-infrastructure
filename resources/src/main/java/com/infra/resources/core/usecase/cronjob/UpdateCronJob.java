package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.controller.cronjob.CronJobUpdateDTO;
import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCronJob {

    private final GetCronJob getCronJob;
    private final CronJobRepository repository;
    private final DeploymentGateway deploymentGateway;

    public CronJob execute(String serviceName, String envName, String name, CronJobUpdateDTO cronJobUpdateDTO) {
        CronJob cronJob = getCronJob.execute(serviceName, envName, name);
        CronJob updated = updateCronJob(cronJob, cronJobUpdateDTO);
        deploymentGateway.updateConfig(serviceName, envName, "Updated cron job " + updated.getName());
        return updated;
    }

    private CronJob updateCronJob(CronJob cronJob, CronJobUpdateDTO cronJobUpdateDTO) {
        if (Objects.nonNull(cronJobUpdateDTO.getEndpoint())) {
            cronJob.setEndpoint(cronJobUpdateDTO.getEndpoint());
        }

        if (Objects.nonNull(cronJobUpdateDTO.getSchedule())) {
            cronJob.setSchedule(cronJobUpdateDTO.getSchedule());
        }

        return repository.save(cronJob);
    }
}
