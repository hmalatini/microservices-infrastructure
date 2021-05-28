package com.infra.deployment.adapter.gateway.cronjob;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.CronJob;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CronJobGateway {

    private final CronJobClient client;

    public Map<String, CronJob> getCronJobsOfServiceAndEnvironment(String serviceName, String envName) {
        return client.getCronJobs(serviceName, envName)
                     .stream()
                     .collect(Collectors.toMap(
                         CronJobDTO::getName,
                         cronJob -> CronJob.builder()
                                           .schedule(cronJob.getSchedule())
                                           .endpoint(cronJob.getEndpoint())
                                           .build()
                     ));
    }
}
