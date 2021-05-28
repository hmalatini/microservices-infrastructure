package com.infra.resources.adapter.infraresources.destroy;

import com.infra.resources.adapter.gateway.terraformcloud.TerraformCloudGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResourceDestroyerManager {

    private final InfrastructureDestroyer infrastructureDestroyer;
    private final TerraformCloudGateway terraformCloudGateway;
    private final VSCResourceCleaner vscResourceCleaner;

    public void destroyResource(String serviceName, String resourceName, String terraformWorkspaceId, String terraformWorkspaceName, String infraRepositoryName, String pathInsideGit) {
        destroyInfrastructure(serviceName, resourceName, terraformWorkspaceId);
        removeResourceInfraFilesInVSC(infraRepositoryName, pathInsideGit, resourceName);
        destroyTerraformWorkspace(terraformWorkspaceName);
    }

    private void destroyInfrastructure(String serviceName, String resourceName, String terraformWorkspaceId) {
        infrastructureDestroyer.destroyInfrastructure(serviceName, resourceName, terraformWorkspaceId);
    }

    private void destroyTerraformWorkspace(String terraformWorkspaceName) {
        terraformCloudGateway.deleteWorkspace(terraformWorkspaceName);
    }

    private void removeResourceInfraFilesInVSC(String infraRepositoryName, String pathInsideGit, String resourceName) {
        vscResourceCleaner.removeResourceInfraFilesInVSC(infraRepositoryName, pathInsideGit, resourceName);
    }
}
