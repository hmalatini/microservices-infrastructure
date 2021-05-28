package com.infra.deployment.adapter.deploymentyamlgenerator.dto;

import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyObject;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceAccount {
    private boolean create;
    private EmptyObject annotations;
    private EmptyString name;
}
