variable "argocd_host" {
  type        = string
  description = "Host of Argocd Server"
}

variable "argocd_port" {
  type        = string
  description = "Port of Argocd Server"
}

variable "argocd_user" {
  type        = string
  description = "User for Argocd server"
}

variable "argocd_pass" {
  type        = string
  description = "Pass for Argocd server"
}

variable "argocd_namespace" {
  type        = string
  description = "Namespace of the app where it will be created"
}

variable "argocd_app_name" {
  type        = string
  description = "App name on Argocd"
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
  description = "Host of kubernetes cluster where app will be deployed"
}

variable "app_namespace" {
  type        = string
  description = "Namespace of the app where it will be created"
}
