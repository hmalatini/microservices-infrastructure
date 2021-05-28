package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyArray;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Ingress {
    private boolean enabled;
    private EmptyObject annotations;
    private EmptyArray hosts;
    private EmptyArray tls;
}
