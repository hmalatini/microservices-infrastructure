package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Image {
    private String repository;
    private String tag;
    private String pullPolicy;
}
