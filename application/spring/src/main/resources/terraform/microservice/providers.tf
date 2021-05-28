terraform {
  required_version = ">= 0.13"
  required_providers {
    github = {
      source = "integrations/github"
      version = "4.4.0"
    }

    argocd = {
      source = "oboukili/argocd"
      version = "1.2.0"
    }
  }
}

provider "github" {
  token = var.github_token
  owner = var.github_owner
}

provider "argocd" {
  server_addr = "${var.argocd_host}:${var.argocd_port}"
  username    = var.argocd_user
  password    = var.argocd_pass
  insecure    = true
}
