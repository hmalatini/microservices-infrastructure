package com.infra.deployment.adapter.gateway.networking;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class HeadersConfigDTO {

    private List<HeaderConfigValueDTO> request;
    private List<HeaderConfigValueDTO> response;
}
