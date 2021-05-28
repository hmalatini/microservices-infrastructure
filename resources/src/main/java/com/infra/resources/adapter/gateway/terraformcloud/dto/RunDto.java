package com.infra.resources.adapter.gateway.terraformcloud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RunDto {
    private DataRunDto data;
}
