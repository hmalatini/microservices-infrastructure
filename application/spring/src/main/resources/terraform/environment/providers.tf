terraform {
  required_version = ">= 0.13"
  required_providers {
    argocd = {
      source  = "oboukili/argocd"
      version = "1.2.0"
    }
  }
}

provider "argocd" {
  server_addr = "${var.argocd_host}:${var.argocd_port}"
  username    = var.argocd_user
  password    = var.argocd_pass
  insecure    = true
}
