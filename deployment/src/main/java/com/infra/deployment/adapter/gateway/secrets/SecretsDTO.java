package com.infra.deployment.adapter.gateway.secrets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
class SecretsDTO {
    private String name;
    private String value;
}
