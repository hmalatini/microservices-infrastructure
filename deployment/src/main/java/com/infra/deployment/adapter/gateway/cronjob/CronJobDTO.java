package com.infra.deployment.adapter.gateway.cronjob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
class CronJobDTO {
    private String name;
    private String schedule;
    private String endpoint;
}
