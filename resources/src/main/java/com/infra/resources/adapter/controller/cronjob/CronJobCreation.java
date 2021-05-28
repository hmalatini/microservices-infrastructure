package com.infra.resources.adapter.controller.cronjob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CronJobCreation {

    private String name;
    private String schedule;
    private String endpoint;
}
