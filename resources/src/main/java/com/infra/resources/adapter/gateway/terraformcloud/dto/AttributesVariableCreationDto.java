package com.infra.resources.adapter.gateway.terraformcloud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttributesVariableCreationDto {

    private String key;
    private String value;
    private String description;
    private String category;
    private boolean hcl;
    private boolean sensitive;
}
