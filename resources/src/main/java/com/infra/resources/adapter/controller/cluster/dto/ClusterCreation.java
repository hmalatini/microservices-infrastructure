package com.infra.resources.adapter.controller.cluster.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ClusterCreation {
    private String name;
    private String host;
    private String createdBy;
}
