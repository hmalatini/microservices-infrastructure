package com.infra.resources.core.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@IdClass(CronJob.CronJobId.class)
public class CronJob {

    @Id
    private String microserviceName;
    @Id
    private String environmentName;
    @Id
    private String name;
    private String schedule;
    private String endpoint;

    @Getter
    @Setter
    @EqualsAndHashCode
    @RequiredArgsConstructor()
    public static class CronJobId implements Serializable {
        private String microserviceName;
        private String environmentName;
        private String name;

        public CronJobId(String microserviceName, String environmentName, String name) {
            this.microserviceName = microserviceName;
            this.environmentName = environmentName;
            this.name = name;
        }
    }
}
