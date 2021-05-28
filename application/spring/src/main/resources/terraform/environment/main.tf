module "argocd_app" {
  source     = "./modules/argocd-app"

  app_git_deployment_repo  = var.app_git_deployment_repo
  app_namespace           = var.app_namespace
  argocd_namespace        = var.argocd_namespace
  argocd_app_name         = var.argocd_app_name
  deployment_files_path   = var.deployment_files_path
  kube_argocd_server_host = var.kube_argocd_server_host
}
