locals {
  deployment_repository_name = "${var.github_repository_name}-deployment"
  dockerhub_user_secret_username = "DOCKERHUB_USERNAME"
  dockerhub_user_secret_registry = "DOCKERHUB_REGISTRY"
  dockerhub_user_secret_token = "DOCKERHUB_TOKEN"
}