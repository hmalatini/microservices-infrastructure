package com.infra.resources.core.usecase.cronjob;

import com.infra.resources.adapter.gateway.deployment.DeploymentGateway;
import com.infra.resources.adapter.repository.CronJobRepository;
import com.infra.resources.core.domain.CronJob;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCronJob {

    private final GetCronJob getCronJob;
    private final CronJobRepository repository;
    private final DeploymentGateway deploymentGateway;
    private final GetAllCronJobsOfServiceAndEnvironment getAllCronJobsOfServiceAndEnvironment;

    public CronJob execute(String serviceName, String envName, String name) {
        CronJob cronJob = getCronJob.execute(serviceName, envName, name);
        repository.delete(cronJob);
        deploymentGateway.updateConfig(serviceName, envName, "Delete cron job " + cronJob.getName());
        return cronJob;
    }

    public List<CronJob> executeAll(String serviceName, String envName) {
        List<CronJob> cronJobList = getAllCronJobsOfServiceAndEnvironment.execute(serviceName, envName);
        repository.deleteAll(cronJobList);
        return cronJobList;
    }
}
