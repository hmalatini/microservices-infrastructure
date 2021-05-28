package com.infra.releases.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Microservice {

    private String name;
    private String gitUrl;
}
