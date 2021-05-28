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
public class FaultConfigDTO {

    private DelayFaultDTO delay;
    private AbortFaultDTO abort;
}
