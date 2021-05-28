package com.infra.resources.adapter.infraresources.creation;

import com.infra.resources.adapter.filesdownloader.FilesDownloader;
import com.infra.resources.adapter.github.GitHubPullRequestCreatorAndMerger;
import com.infra.resources.adapter.terraformcloud.TerraformCloudWorkspaceCreator;
import com.infra.resources.adapter.vscmanager.LocalVSCRepoAdministrator;
import com.infra.resources.core.domain.TerraformVariable;
import com.infra.resources.core.exceptions.CreateResourceException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;

import java.util.List;

@RequiredArgsConstructor
abstract class ResourceCreator {
    abstract List<TerraformVariable> getPlanVariablesMap(String... values);

    abstract boolean isAutoApply();

    private final FilesDownloader filesDownloader;
    private final LocalVSCRepoAdministrator localVSCRepoAdministrator;
    private final TerraformCloudWorkspaceCreator terraformCloudWorkspaceCreator;
    private final GitHubPullRequestCreatorAndMerger pullRequestManager;

    String createResource(String repoName, String resourceName, String dstPathInGitRepo, String terraformWorkspaceName, List<TerraformVariable> terraformVariables, String rootFolderInDS) {
        String workspaceId = createTerraformWorkspace(repoName, terraformWorkspaceName, dstPathInGitRepo, terraformVariables);
        String branchName = addTerraformFilesToRepository(repoName, resourceName, dstPathInGitRepo, rootFolderInDS);
        createPullRequestAndMergeIfRequirementsApproved(repoName, resourceName, branchName);
        return workspaceId;
    }

    private String addTerraformFilesToRepository(String repoName, String resourceName, String dstPath, String rootFolderInDS) {
        String path = localVSCRepoAdministrator.getRandomPathToDownload();
        String branchName = path.substring(path.lastIndexOf('/') + 1);
        try {
            Git gitInfraRepository = downloadRemoteInfraRepository(repoName, path);
            createNewBranch(gitInfraRepository, branchName);
            addTerraformFilesToPath(path + dstPath, rootFolderInDS);
            updateRemoteInfraRepository(gitInfraRepository, "resource " + resourceName + " created");
        } catch (Exception e) {
            throw new CreateResourceException("error creating microservice", e);
        } finally {
            localVSCRepoAdministrator.clean(path);
        }

        return branchName;
    }

    private void createNewBranch(Git gitInfraRepository, String branchName) {
        localVSCRepoAdministrator.createNewBranch(gitInfraRepository, branchName);
    }

    private Git downloadRemoteInfraRepository(String repoName, String path) {
        return localVSCRepoAdministrator.downloadRemoteRepository(repoName, path);
    }

    private void addTerraformFilesToPath(String path, String rootFolderInDocumentStorage) {
        filesDownloader.downloadFilesToPath(path, rootFolderInDocumentStorage);
    }

    private void updateRemoteInfraRepository(Git gitInfraRepository, String commitMsg) {
        localVSCRepoAdministrator.updateRemoteRepository(gitInfraRepository, commitMsg);
    }

    private String createTerraformWorkspace(String repoName, String workspaceName, String workingDir, List<TerraformVariable> terraformVariables) {
        return terraformCloudWorkspaceCreator.create(repoName, workspaceName, workingDir, terraformVariables, isAutoApply());
    }

    private void createPullRequestAndMergeIfRequirementsApproved(String repoName, String resourceName, String branchName) {
        pullRequestManager.createAndMergeIfApproved(repoName, "create " + resourceName, branchName);
    }
}
