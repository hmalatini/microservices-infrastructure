package com.infra.resources.adapter.gateway.terraformcloud.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WorkspaceCreationResponse {
    private DataWorkspaceCreationResponse data;
}