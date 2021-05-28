package com.infra.resources.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@IdClass(Environment.EnvironmentId.class)
public class Environment {

    @Id
    private String microserviceName;
    @Id
    private String name;
    private boolean isTest;
    private String createdBy;
    private String terraformWorkspaceId;
    private String clusterName;
    private boolean autoscalingEnabled;
    private int autoscalingTargetCPU;
    private int minReplicas;
    private int maxReplicas;

    @Getter
    @Setter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class EnvironmentId implements Serializable {
        private String microserviceName;
        private String name;

        public EnvironmentId(String microserviceName, String name) {
            this.microserviceName = microserviceName;
            this.name = name;
        }
    }
}
