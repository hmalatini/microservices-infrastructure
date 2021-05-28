package com.infra.resources.adapter.controller.environment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class EnvironmentCreation {
    private String environmentName;
    private String imageVersion;
    private String clusterName;
    private Boolean isTest;
    private String createdBy;
}
