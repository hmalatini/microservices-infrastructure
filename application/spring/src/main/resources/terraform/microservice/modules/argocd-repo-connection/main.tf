// Private Git repository
resource "argocd_repository" "private_git" {
  repo     = var.git_deployment_repo
  type     = "git"
  username = var.git_user
  password = var.git_pass
  insecure = true
}
