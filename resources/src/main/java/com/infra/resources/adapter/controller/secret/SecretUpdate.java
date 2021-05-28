package com.infra.resources.adapter.controller.secret;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SecretUpdate {

    private String value;
    private String description;
}
