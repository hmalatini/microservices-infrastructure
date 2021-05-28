package com.infra.resources.adapter.controller.microservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MicroserviceCreation {
    private String name;
    private String template;
    private String createdBy;
    private String team;
}
