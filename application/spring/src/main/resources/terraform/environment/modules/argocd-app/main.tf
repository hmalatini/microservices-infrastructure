resource "argocd_application" "app" {
  metadata {
    name      = var.argocd_app_name
    namespace = var.argocd_namespace
  }

  wait = false

  spec {
    project = "default"

    source {
      repo_url        = var.app_git_deployment_repo
      path            = var.deployment_files_path
      target_revision = "HEAD"
    }

    destination {
      server    = var.kube_argocd_server_host
      namespace = var.app_namespace
    }

    sync_policy {
      automated = {
        prune       = true
        self_heal   = true
        allow_empty = true
      }
      # Only available from ArgoCD 1.5.0 onwards
      sync_options = ["Validate=false", "CreateNamespace=true"]
      retry {
        limit = "5"
        backoff = {
          duration     = "30s"
          max_duration = "2m"
          factor       = "2"
        }
      }
    }
  }
}
