package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndpointMatchValue {

    public enum MatchType {
        REGEX,
        PREFIX,
        EXACT;

        public String getType() {
            return this.name()
                       .toLowerCase();
        }
    }

    private String type;
    private String value;
}
