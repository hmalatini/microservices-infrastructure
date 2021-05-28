resource "github_repository" "deployment_repository" {
  name = local.deployment_repository_name

  visibility = "private"

  template {
    owner      = var.github_owner
    repository = "deployment-template"
  }
}

resource "github_repository" "service_repository" {
  name = var.github_repository_name

  visibility = "private"

  template {
    owner      = var.github_owner
    repository = var.github_template
  }
}

resource "github_actions_secret" "ci_dockerhub_username" {
  depends_on = [github_repository.service_repository]
  repository      = var.github_repository_name
  secret_name     = local.dockerhub_user_secret_username
  plaintext_value = var.dockerhub_username
}

resource "github_actions_secret" "ci_dockerhub_registry" {
  depends_on = [github_repository.service_repository]
  repository      = var.github_repository_name
  secret_name     = local.dockerhub_user_secret_registry
  plaintext_value = var.dockerhub_registry
}

resource "github_actions_secret" "ci_dockerhub_token" {
  depends_on = [github_repository.service_repository]
  repository      = var.github_repository_name
  secret_name     = local.dockerhub_user_secret_token
  plaintext_value = var.dockerhub_token
}
