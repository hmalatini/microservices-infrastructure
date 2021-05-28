### GITHUB
variable "github_token" {
  type        = string
  description = "Github Token"
}

variable "github_owner" {
  type        = string
  description = "Owner from github"
}

variable "github_repository_name" {
  type        = string
  description = "Name of the repository"
}

variable "github_template" {
  type        = string
  description = "Template to init the repository"
}

variable "dockerhub_username" {
  type        = string
  description = "Dockerhub Username for push CI builds"
}

variable "dockerhub_registry" {
  type        = string
  description = "Dockerhub registry for push CI builds"
}

variable "dockerhub_token" {
  type        = string
  description = "Dockerhub user password for push CI builds"
}

### ARGO

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
