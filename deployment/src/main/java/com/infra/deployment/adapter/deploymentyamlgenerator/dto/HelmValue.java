package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.Networking;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyArray;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyObject;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyString;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HelmValue {
    private int replicaCount;
    private Image image;
    private EmptyArray imagePullSecrets;
    private String nameOverride;
    private String fullnameOverride;
    private String namespace;
    private ServiceAccount serviceAccount;
    private EmptyObject podAnnotations;
    private EmptyObject podSecurityContext;
    private EmptyObject securityContext;
    private Service service;
    private Ingress ingress;
    private PodResources resources;
    private Autoscaling autoscaling;
    private EmptyObject nodeSelector;
    private EmptyArray tolerations;
    private EmptyObject affinity;
    private String environment;
    private Map<String, String> configValues;
    private Map<String, CronJob> cronjobs;
    private Networking networking;

    public static HelmValue.HelmValueBuilder getDefaultValuesBuilder() {
        return getEmptyValuesBuilder()
                .resources(PodResources.builder()
                                       .limits(PodResourceLimits.builder().cpu("1").memory("512M").build())
                                       .requests(PodResourceRequest.builder().cpu("50m").memory("240M").build())
                                       .build())
                .serviceAccount(ServiceAccount.builder().create(true).annotations(new EmptyObject()).name(new EmptyString()).build())
                .service(Service.builder().type("ClusterIP").port(80).build())
                .ingress(Ingress.builder().enabled(false).annotations(new EmptyObject()).hosts(new EmptyArray()).tls(new EmptyArray()).build());
    }

    private static HelmValue.HelmValueBuilder getEmptyValuesBuilder() {
        return HelmValue.builder()
                .imagePullSecrets(new EmptyArray())
                .podAnnotations(new EmptyObject())
                .podSecurityContext(new EmptyObject())
                .securityContext(new EmptyObject())
                .nodeSelector(new EmptyObject())
                .tolerations(new EmptyArray())
                .affinity(new EmptyObject());
    }
}
