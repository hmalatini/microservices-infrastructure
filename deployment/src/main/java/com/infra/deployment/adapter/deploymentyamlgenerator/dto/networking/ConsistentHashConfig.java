package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsistentHashConfig {

    private ConsistentHashMatch match;
    private Long minimumRingSize;
}
