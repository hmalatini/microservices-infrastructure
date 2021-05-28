package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FaultConfig {

    private DelayFault delay;
    private AbortFault abort;
}
