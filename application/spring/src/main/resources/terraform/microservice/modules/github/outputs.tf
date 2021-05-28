output "deployment_repo_name" {
  value       = github_repository.deployment_repository.name
  description = "The Github deployment repo name."
}

output "service_repo_name" {
  value       = github_repository.service_repository.name
  description = "The Github service repo name."
}

output "dockerhub_user_secret" {
  value       = github_actions_secret.ci_dockerhub_username.plaintext_value
  description = "The DockerHub username."
  sensitive   = true
}

output "dockerhub_token_secret" {
  value       = github_actions_secret.ci_dockerhub_token.plaintext_value
  description = "The DockerHub token."
  sensitive   = true
}

output "dockerhub_registry_secret" {
  value       = github_actions_secret.ci_dockerhub_registry.plaintext_value
  description = "The DockerHub registry."
}
