package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PodResourceLimits {
    private String cpu;
    private String memory;
}
