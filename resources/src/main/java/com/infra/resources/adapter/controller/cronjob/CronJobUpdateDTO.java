package com.infra.resources.adapter.controller.cronjob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CronJobUpdateDTO {

    private String schedule;
    private String endpoint;
}
