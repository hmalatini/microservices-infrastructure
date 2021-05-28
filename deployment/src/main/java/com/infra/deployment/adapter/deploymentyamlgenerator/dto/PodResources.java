package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PodResources {
    private PodResourceLimits limits;
    private PodResourceRequest requests;
}
