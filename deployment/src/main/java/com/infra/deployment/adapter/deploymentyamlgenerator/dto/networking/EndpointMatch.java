package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndpointMatch {

    private EndpointMatchValue uri;
    private EndpointMatchValue method;
    private Map<String, EndpointMatchValue> headers;
    private Map<String, EndpointMatchValue> noHeaders;
    private Map<String, EndpointMatchValue> queryParams;
}
