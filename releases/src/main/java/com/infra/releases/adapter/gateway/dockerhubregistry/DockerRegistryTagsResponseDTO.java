package com.infra.releases.adapter.gateway.dockerhubregistry;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
class DockerRegistryTagsResponseDTO {

    private List<String> tags;
}
