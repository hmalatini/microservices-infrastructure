package com.infra.resources.adapter.gateway.terraformcloud.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DataWorkspaceCreationDto {
    private AttributesWorkspaceCreationDto attributes;
    private String type;
}
