package com.infra.application.adapter.vscmanager;

import com.infra.application.core.exceptions.GitHubException;
import com.infra.deployment.adapter.vscmanager.DeploymentGitManager;
import com.infra.resources.adapter.vscmanager.MicroservicesGitManager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class GitManager implements MicroservicesGitManager, DeploymentGitManager {

    private static final String LOCAL_REPO_PATH_PREFIX = File.separator + "tmp" + File.separator + "repo" + File.separator;

    public Git cloneRepo(String repoUrl, String user, String pass, File dir) {
        checkDir(dir);

        Git git;
        try {
            git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(dir)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, pass))
                    .call();
        } catch (GitAPIException e) {
            throw new GitHubException("error cloning repository", e);
        }

        return git;
    }

    public void addCommitAndPush(Git git, String commitMsg, String user, String pass) {
        add(git);
        commit(git, commitMsg);
        push(git, user, pass);
    }

    public void createBranch(Git git, String branchName) {
        try {
            git.checkout().setName(branchName).setCreateBranch(true).setForced(true).call();
        } catch (GitAPIException e) {
            throw new GitHubException("error creating branch", e);
        }
    }

    public String getRepositoryPrefixPath() {
        return LOCAL_REPO_PATH_PREFIX;
    }

    private void checkDir(File file) {
        if (!file.exists()) {
            throw new GitHubException("directory does not exist");
        }

        if (!file.isDirectory()) {
            throw new GitHubException("the path is not a directory");
        }
    }

    private void add(Git git) {
        try {
            git.add().addFilepattern(".").call();
            git.add().addFilepattern(".").setUpdate(true).call();
        } catch (GitAPIException e) {
            throw new GitHubException("error adding files to staging area", e);
        }
    }

    private void commit(Git git, String msg) {
        try {
            git.commit().setMessage(msg).call();
        } catch (GitAPIException e) {
            throw new GitHubException("error creating commit", e);
        }
    }

    private void push(Git git, String user, String pass) {
        try {
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, pass)).call();
        } catch (GitAPIException e) {
            throw new GitHubException("error pushing changes to remote repository", e);
        }
    }
}
