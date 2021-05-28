package com.infra.resources.adapter.controller.networking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeaderConfigValueDTO {

    public enum Operation {
        SET, ADD, REMOVE
    }

    private Operation operation;
    private String headerName;
    private String headerValue;
}
