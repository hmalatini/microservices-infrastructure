module "github-repositories" {
  source = "./modules/github"

  dockerhub_registry     = var.dockerhub_registry
  dockerhub_token        = var.dockerhub_token
  dockerhub_username     = var.dockerhub_username
  github_owner           = var.github_owner
  github_repository_name = var.github_repository_name
  github_template        = var.github_template
}

module "argocd-repo-connection" {
  source     = "./modules/argocd-repo-connection"
  depends_on = [module.github-repositories]

  git_deployment_repo = "https://github.com/${var.github_owner}/${module.github-repositories.deployment_repo_name}.git"
  git_pass            = var.github_token
  git_user            = var.github_owner
}
