package com.infra.deployment.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@IdClass(Deployment.DeploymentId.class)
public class Deployment {

    @Id
    @JsonProperty("serviceName")
    private String microserviceName;
    @Id
    @JsonProperty("environment")
    private String environmentName;
    private String version;

    @Transient
    boolean useDefaultAutoscalingValues;

    @Getter
    @Setter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class DeploymentId implements Serializable {
        private String microserviceName;
        private String environmentName;

        public DeploymentId(String microserviceName, String environmentName) {
            this.microserviceName = microserviceName;
            this.environmentName = environmentName;
        }
    }
}
