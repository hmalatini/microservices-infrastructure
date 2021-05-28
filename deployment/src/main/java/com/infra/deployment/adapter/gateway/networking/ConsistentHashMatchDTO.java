package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ConsistentHashMatchDTO {

    private String headerName;
    private String queryParameterName;
    private Boolean useSourceIp;
    private ConsistentHashMatchCookieDTO cookie;
}
