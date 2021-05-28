package com.infra.resources.adapter.infraresources.creation;

import com.infra.resources.adapter.filesdownloader.FilesDownloader;
import com.infra.resources.adapter.github.GitHubPullRequestCreatorAndMerger;
import com.infra.resources.adapter.terraformcloud.TerraformCloudWorkspaceCreator;
import com.infra.resources.adapter.vscmanager.LocalVSCRepoAdministrator;
import com.infra.resources.config.ArgoCDConfig;
import com.infra.resources.core.domain.TerraformVariable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class EnvironmentResourceCreator extends ResourceCreator {

    private final ArgoCDConfig argoCDConfig;

    public EnvironmentResourceCreator(FilesDownloader filesDownloader, LocalVSCRepoAdministrator localVSCRepoAdministrator, TerraformCloudWorkspaceCreator terraformCloudWorkspaceCreator, GitHubPullRequestCreatorAndMerger gitHubPullRequestCreatorAndMerger, ArgoCDConfig argoCDConfig) {
        super(filesDownloader, localVSCRepoAdministrator, terraformCloudWorkspaceCreator, gitHubPullRequestCreatorAndMerger);
        this.argoCDConfig = argoCDConfig;
    }

    @Override
    List<TerraformVariable> getPlanVariablesMap(String... values) {
        List<TerraformVariable> variableList = new ArrayList<>();
        variableList.add(new TerraformVariable("argocd_app_name", values[0], false, "App name", false));
        variableList.add(new TerraformVariable("app_git_deployment_repo", values[1], false, "App deployment repository", false));
        variableList.add(new TerraformVariable("deployment_files_path", values[2], false, "App deployment repository", false));
        variableList.add(new TerraformVariable("kube_argocd_server_host", values[3], false, "Destination cluster host", false));
        variableList.add(new TerraformVariable("app_namespace", values[4], false, "Kubernetes namespace for app", false));
        variableList.add(new TerraformVariable("argocd_host", argoCDConfig.getArgocdHost(), false, "ArgoCD Hostname", false));
        variableList.add(new TerraformVariable("argocd_namespace", argoCDConfig.getArgocdNamespace(), false, "ArgoCD Namespace", false));
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
