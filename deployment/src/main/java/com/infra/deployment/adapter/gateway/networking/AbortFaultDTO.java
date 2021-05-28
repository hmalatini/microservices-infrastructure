package com.infra.deployment.adapter.gateway.networking;

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
public class AbortFaultDTO {

    private Integer httpStatus;
    private Integer percentage;
}
