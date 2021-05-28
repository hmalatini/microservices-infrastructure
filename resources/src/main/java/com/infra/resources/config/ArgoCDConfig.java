package com.infra.resources.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ArgoCDConfig {

    @Value("${argocd.host}")
    private String argocdHost;

    @Value("${argocd.port}")
    private String argocdPort;

    @Value("${argocd.user}")
    private String argocdUser;

    @Value("${argocd.pass}")
    private String argocdPass;

    @Value("${argocd.namespace}")
    private String argocdNamespace;
}
