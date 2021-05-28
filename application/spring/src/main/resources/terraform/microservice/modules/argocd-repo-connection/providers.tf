terraform {
  required_version = ">= 0.13"
  required_providers {
    argocd = {
      source = "oboukili/argocd"
      version = "1.2.0"
    }
  }
}
