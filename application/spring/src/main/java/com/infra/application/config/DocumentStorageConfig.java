package com.infra.application.config;

import com.infra.deployment.config.DocumentStorageConfigFromDeployment;
import com.infra.resources.config.DocumentStorageConfigFromMicroservices;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DocumentStorageConfig implements DocumentStorageConfigFromDeployment, DocumentStorageConfigFromMicroservices {
    @Value("${document-storage.url}")
    private String url;

    @Value("${document-storage.user}")
    private String user;

    @Value("${document-storage.pass}")
    private String pass;

    @Value("${document-storage.host}")
    private String host;

    @Value("${document-storage.port}")
    private String port;

    @Value("${document-storage.deployment-bucket}")
    private String deploymentBucket;

    @Value("${document-storage.infra-bucket}")
    private String infrastructureBucket;
}
