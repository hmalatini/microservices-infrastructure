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
public class ConsistentHashMatchDTO {

    private String headerName;
    private String queryParameterName;
    private Boolean useSourceIp;
    private ConsistentHashMatchCookieDTO cookie;
}
