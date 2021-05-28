package com.infra.resources.adapter.controller.networking;

import java.util.List;
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
public class HeadersConfigDTO {

    private List<HeaderConfigValueDTO> request;
    private List<HeaderConfigValueDTO> response;
}
