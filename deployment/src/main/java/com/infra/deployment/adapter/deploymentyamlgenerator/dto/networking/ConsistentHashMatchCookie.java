package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsistentHashMatchCookie {

    private String name;
    private String path;
    private String ttl;
}
