package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EndpointMatchValueDTO {

    public enum MatchType {
        REGEX, PREFIX, EXACT
    }

    private MatchType type;
    private String value;
}
