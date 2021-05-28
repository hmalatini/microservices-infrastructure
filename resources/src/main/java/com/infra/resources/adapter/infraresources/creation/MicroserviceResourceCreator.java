package com.infra.resources.adapter.infraresources.creation;

import com.infra.resources.adapter.filesdownloader.FilesDownloader;
import com.infra.resources.adapter.github.GitHubPullRequestCreatorAndMerger;
import com.infra.resources.adapter.terraformcloud.TerraformCloudWorkspaceCreator;
import com.infra.resources.adapter.vscmanager.LocalVSCRepoAdministrator;
import com.infra.resources.config.ArgoCDConfig;
import com.infra.resources.config.GitHubConfig;
import com.infra.resources.config.RegistryConfig;
import com.infra.resources.core.domain.TerraformVariable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class MicroserviceResourceCreator extends ResourceCreator {

    private final RegistryConfig registryConfig;
    private final ArgoCDConfig argoCDConfig;
    private final GitHubConfig gitHubConfig;

    public MicroserviceResourceCreator(FilesDownloader filesDownloader, LocalVSCRepoAdministrator localVSCRepoAdministrator, TerraformCloudWorkspaceCreator terraformCloudWorkspaceCreator, GitHubPullRequestCreatorAndMerger gitHubPullRequestCreatorAndMerger, RegistryConfig registryConfig, ArgoCDConfig argoCDConfig, GitHubConfig gitHubConfig) {
        super(filesDownloader, localVSCRepoAdministrator, terraformCloudWorkspaceCreator, gitHubPullRequestCreatorAndMerger);
        this.registryConfig = registryConfig;
        this.argoCDConfig = argoCDConfig;
        this.gitHubConfig = gitHubConfig;
    }

    @Override
    List<TerraformVariable> getPlanVariablesMap(String... values) {
        List<TerraformVariable> variableList = new ArrayList<>();
        variableList.add(new TerraformVariable("github_repository_name", values[0], false, "The name of the repository for business code", false));
        variableList.add(new TerraformVariable("github_template", values[1], false, "Template name to create repository repository business code from", false));
        variableList.add(new TerraformVariable("github_owner", gitHubConfig.getGithubOwner(), false, "Owner of the GitHub account", false));
        variableList.add(new TerraformVariable("github_token", gitHubConfig.getGithubToken(), true, "Github API token auth", false));
        variableList.add(new TerraformVariable("dockerhub_username", registryConfig.getRegistryUser(), false, "Dockerhub username to upload builds to the registry", false));
        variableList.add(new TerraformVariable("dockerhub_token", registryConfig.getDockerRegistryToken(), true, "Dockerhub token", false));
        variableList.add(new TerraformVariable("dockerhub_registry", registryConfig.getRegistryRepo(), false, "Dockerhub registry repo to upload containers", false));
        variableList.add(new TerraformVariable("argocd_host", argoCDConfig.getArgocdHost(), false, "ArgoCD Hostname", false));
        variableList.add(new TerraformVariable("argocd_port", argoCDConfig.getArgocdPort(), false, "ArgoCD Port", false));
        variableList.add(new TerraformVariable("argocd_user", argoCDConfig.getArgocdUser(), false, "ArgoCD Username", false));
        variableList.add(new TerraformVariable("argocd_pass", argoCDConfig.getArgocdPass(), true, "ArgoCD Password", false));

        return variableList;
    }

    @Override
    boolean isAutoApply() {
        return true;
    }


}
