package com.infra.deployment.adapter.deploymentyamlgenerator.resourcemanager;

import com.infra.deployment.adapter.filemanager.DeploymentFileManager;
import com.infra.deployment.config.DocumentStorageConfigFromDeployment;
import com.infra.deployment.core.exceptions.CreateDeploymentException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeploymentTemplateManager {

    private final DeploymentFileManager deploymentFileManager;
    private final DocumentStorageFromDeployment documentStorage;
    private final DocumentStorageConfigFromDeployment documentStorageConfig;

    public File createDirAndDownloadTemplateFiles(String path, String env) {
        File envDir = deploymentFileManager.createDir(path + File.separator + env);
        copyHelmTemplates(envDir);

        return envDir;
    }

    private void copyHelmTemplates(File dst) {
        try {
            List<String> files = documentStorage.listFilesInPath(documentStorageConfig.getDeploymentBucket(), "");
            for (String path : files) {
                documentStorage.downloadFile(documentStorageConfig.getDeploymentBucket(), path, dst.getPath() + File.separator + path);
            }
        } catch (Exception e) {
            throw new CreateDeploymentException("Error downloading template files from document storage", e);
        }
    }
}
