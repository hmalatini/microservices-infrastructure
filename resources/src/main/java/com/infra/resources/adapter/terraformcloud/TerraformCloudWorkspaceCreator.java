package com.infra.resources.adapter.terraformcloud;

import com.infra.resources.adapter.gateway.terraformcloud.TerraformCloudGateway;
import com.infra.resources.core.domain.TerraformVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TerraformCloudWorkspaceCreator {

    private final TerraformCloudGateway terraformCloudGateway;

    public String create(String repoName, String workspaceName, String workingDir, List<TerraformVariable> terraformVariables, boolean autoApplyPlans) {
        String workspaceId = terraformCloudGateway.createWorkspace(repoName, workspaceName, workingDir, autoApplyPlans);

        terraformVariables.forEach(variable ->
                terraformCloudGateway.createVariable(workspaceId, variable)
        );

        terraformCloudGateway.run(workspaceId, "create resource", false);
        return workspaceId;
    }
}
