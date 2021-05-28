package com.infra.releases.adapter.controller;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class ServiceReleaseListResponseDTO {

    private final String microserviceName;
    private final List<String> releaseVersionNames;
}
