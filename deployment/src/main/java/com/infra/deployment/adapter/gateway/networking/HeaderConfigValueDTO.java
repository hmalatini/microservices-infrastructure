package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class HeaderConfigValueDTO {

    public enum Operation {
        SET, ADD, REMOVE
    }

    private Operation operation;
    private String headerName;
    private String headerValue;
}
