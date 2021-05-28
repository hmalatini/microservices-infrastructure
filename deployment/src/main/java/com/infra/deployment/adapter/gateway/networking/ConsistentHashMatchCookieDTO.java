package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ConsistentHashMatchCookieDTO {

    private String name;
    private String path;
    private String ttl;
}
