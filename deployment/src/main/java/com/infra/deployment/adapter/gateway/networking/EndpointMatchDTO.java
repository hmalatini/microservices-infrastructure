package com.infra.deployment.adapter.gateway.networking;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointMatchDTO {

    private EndpointMatchValueDTO uri;
    private EndpointMatchValueDTO method;
    private Map<String, EndpointMatchValueDTO> headers;
    private Map<String, EndpointMatchValueDTO> noHeaders;
    private Map<String, EndpointMatchValueDTO> queryParams;
}
