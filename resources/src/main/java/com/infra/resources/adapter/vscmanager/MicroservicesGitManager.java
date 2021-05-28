package com.infra.resources.adapter.vscmanager;

import org.eclipse.jgit.api.Git;

import java.io.File;

public interface MicroservicesGitManager {
    Git cloneRepo(String repoUrl, String user, String pass, File dir);

    void addCommitAndPush(Git git, String commitMsg, String user, String pass);

    void createBranch(Git git, String branchName);

    String getRepositoryPrefixPath();
}
