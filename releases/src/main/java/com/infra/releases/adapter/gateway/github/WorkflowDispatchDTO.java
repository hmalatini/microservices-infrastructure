package com.infra.releases.adapter.gateway.github;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkflowDispatchDTO {

    private String ref;
    private Map<String, String> inputs;
}
