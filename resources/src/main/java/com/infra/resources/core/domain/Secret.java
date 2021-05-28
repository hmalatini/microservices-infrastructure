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
@IdClass(Secret.SecretId.class)
public class Secret {

    @Id
    private String microserviceName;
    @Id
    private String name;
    private String value;
    private String description;
    private boolean isSensitive;
    private String createdBy;

    @Getter
    @Setter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class SecretId implements Serializable {
        private String microserviceName;
        private String name;

        public SecretId(String microserviceName, String name) {
            this.microserviceName = microserviceName;
            this.name = name;
        }
    }

}
