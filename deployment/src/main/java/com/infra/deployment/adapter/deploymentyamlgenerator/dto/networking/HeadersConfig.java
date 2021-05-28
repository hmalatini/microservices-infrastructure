package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HeadersConfig {
    public enum Operation {
        SET,
        ADD,
        REMOVE;

        public String getOperationName() {
            return this.name()
                       .toLowerCase();
        }
    }

    private Map<String, List<HeaderConfigValue>> request;
    private Map<String, List<HeaderConfigValue>> response;
}
