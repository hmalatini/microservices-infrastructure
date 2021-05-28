package com.infra.releases.adapter.gateway.dockerhubregistry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
class DockerRegistryTokenResponseDTO {

    private String token;
}
