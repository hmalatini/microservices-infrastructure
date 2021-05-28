variable "argocd_app_name" {
  type        = string
  description = "App name on Argocd"
}

variable "argocd_namespace" {
  type        = string
  description = "Namespace of the app where it will be created"
}

variable "app_git_deployment_repo" {
  type        = string
  description = "Deployment repo of the service"
}

variable "deployment_files_path" {
  type        = string
  description = "Path to the specific deployment files for the app"
}

variable "kube_argocd_server_host" {
  type        = string
  description = "Host of kubernetes cluster of the app"
}

variable "app_namespace" {
  type        = string
  description = "App namespace in kubernetes"
}
