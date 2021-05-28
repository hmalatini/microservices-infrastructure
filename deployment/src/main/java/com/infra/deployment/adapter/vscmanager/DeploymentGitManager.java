package com.infra.deployment.adapter.vscmanager;

import org.eclipse.jgit.api.Git;

import java.io.File;

public interface DeploymentGitManager {
    Git cloneRepo(String repoUrl, String user, String pass, File dir);

    void addCommitAndPush(Git git, String commitMsg, String user, String pass);

    String getRepositoryPrefixPath();
}
