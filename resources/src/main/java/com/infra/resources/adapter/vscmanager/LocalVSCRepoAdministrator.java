package com.infra.resources.adapter.vscmanager;

import com.infra.resources.adapter.filemanager.MicroserviceFileManager;
import com.infra.resources.config.GitHubConfig;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalVSCRepoAdministrator {

    private static final String GITHUB_URL = "https://github.com/";

    private final MicroserviceFileManager fileManager;
    final GitHubConfig gitHubConfig;
    private final MicroservicesGitManager gitManager;

    public String getRandomPathToDownload() {
        return gitManager.getRepositoryPrefixPath() + UUID.randomUUID();
    }

    public Git downloadRemoteRepository(String repoName, String dstPath) {
        File repoDir = fileManager.createDir(dstPath);
        String gitInfraRepoUrl = GITHUB_URL + gitHubConfig.getGithubOwner() + "/" + repoName + ".git";
        return gitManager.cloneRepo(gitInfraRepoUrl, gitHubConfig.getGithubOwner(), gitHubConfig.getGithubToken(), repoDir);
    }

    public void updateRemoteRepository(Git git, String commitMsg) {
        gitManager.addCommitAndPush(git, commitMsg, gitHubConfig.getGithubOwner(), gitHubConfig.getGithubToken());
    }

    public void clean(String path) {
        fileManager.deleteDir(path);
    }

    public void createNewBranch(Git gitInfraRepository, String branchName) {
        gitManager.createBranch(gitInfraRepository, branchName);
    }
}
