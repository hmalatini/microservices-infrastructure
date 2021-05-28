package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsistentHashMatch {

    private String headerName;
    private String queryParameterName;
    private Boolean useSourceIp;
    private ConsistentHashMatchCookie cookie;
}
