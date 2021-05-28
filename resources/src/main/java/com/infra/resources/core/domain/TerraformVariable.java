package com.infra.resources.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TerraformVariable {
    private String key;
    private String value;
    private boolean sensitive;
    private String description;
    private boolean isEnvironment;

    public TerraformVariable(String key, String value, boolean sensitive, String description, boolean isEnvironment) {
        this.key = key;
        this.value = value;
        this.sensitive = sensitive;
        this.description = description;
        this.isEnvironment = isEnvironment;
    }
}
