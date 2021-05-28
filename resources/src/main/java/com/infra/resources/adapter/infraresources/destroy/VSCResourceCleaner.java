package com.infra.resources.adapter.infraresources.destroy;

import com.infra.resources.adapter.github.GitHubPullRequestCreatorAndMerger;
import com.infra.resources.adapter.vscmanager.LocalVSCRepoAdministrator;
import com.infra.resources.core.exceptions.DestroyResourceException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class VSCResourceCleaner {

    private final LocalVSCRepoAdministrator localVSCRepoAdministrator;
    private final GitHubPullRequestCreatorAndMerger pullRequestManager;

    void removeResourceInfraFilesInVSC(String infraRepositoryName, String pathInsideGit, String resourceName) {
        String branchName = removeFilesInVSC(infraRepositoryName, pathInsideGit, resourceName);
        createPullRequestAndMergeIfRequirementsApproved(infraRepositoryName, resourceName, branchName);
    }

    private String removeFilesInVSC(String infraRepositoryName, String pathInsideGit, String resourceName) {
        String path = localVSCRepoAdministrator.getRandomPathToDownload();
        String branchName = path.substring(path.lastIndexOf('/') + 1);
        try {
            Git gitInfraRepository = downloadRemoteInfraRepository(infraRepositoryName, path);
            createNewBranch(gitInfraRepository, branchName);
            removeResourceFiles(path, pathInsideGit);
            updateRemoteInfraRepository(gitInfraRepository, resourceName);
        } catch (Exception e) {
            throw new DestroyResourceException("error removing infra resource files", e);
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

    private void removeResourceFiles(String path, String pathInsideGit) {
        localVSCRepoAdministrator.clean(path + pathInsideGit);
    }

    private void updateRemoteInfraRepository(Git gitInfraRepository, String resourceName) {
        localVSCRepoAdministrator.updateRemoteRepository(gitInfraRepository, "resource " + resourceName + " deleted");
    }

    private void createPullRequestAndMergeIfRequirementsApproved(String repoName, String resourceName, String branchName) {
        pullRequestManager.createAndMergeIfApproved(repoName, "remove " + resourceName, branchName);
    }
}
