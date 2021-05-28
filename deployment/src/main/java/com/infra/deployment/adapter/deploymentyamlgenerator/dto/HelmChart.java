package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HelmChart {
    private String apiVersion;
    private String name;
    private String description;
    private String type;
    private String version;
    private String appVersion;

    public static HelmChart.HelmChartBuilder getDefaultValuesBuilder() {
        return HelmChart.builder()
                .apiVersion("v2")
                .description("A Helm chart for Kubernetes")
                .type("application")
                .version("1.0.0");
    }
}
