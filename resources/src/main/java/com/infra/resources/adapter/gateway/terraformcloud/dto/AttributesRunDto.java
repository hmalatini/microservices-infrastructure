package com.infra.resources.adapter.gateway.terraformcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttributesRunDto {
    @JsonProperty("is-destroy")
    private boolean isDestroy;
    private String message;
}
