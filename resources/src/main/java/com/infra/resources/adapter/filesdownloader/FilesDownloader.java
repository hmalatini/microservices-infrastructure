package com.infra.resources.adapter.filesdownloader;

import com.infra.resources.adapter.documentstorage.DocumentStorageFromMicroservices;
import com.infra.resources.adapter.filemanager.MicroserviceFileManager;
import com.infra.resources.config.DocumentStorageConfigFromMicroservices;
import com.infra.resources.core.exceptions.CreateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilesDownloader {

    private final MicroserviceFileManager fileManager;
    private final DocumentStorageFromMicroservices documentStorage;
    private final DocumentStorageConfigFromMicroservices documentStorageConfig;

    public void downloadFilesToPath(String path, String rootFolderInDocumentStorage) {
        File microserviceResourceDir = fileManager.createDir(path);
        copyTerraformFiles(microserviceResourceDir, rootFolderInDocumentStorage);
    }

    private void copyTerraformFiles(File dst, String rootFolderInDS) {
        try {
            List<String> files = documentStorage.listFilesInPath(documentStorageConfig.getInfrastructureBucket(), rootFolderInDS);
            for (String path : files) {
                if (!path.startsWith(rootFolderInDS)) continue;

                String destPath = dst.getPath() + File.separator + path.replace(rootFolderInDS + "/", "");
                documentStorage.downloadFile(documentStorageConfig.getInfrastructureBucket(), path, destPath);
            }
        } catch (Exception e) {
            throw new CreateResourceException("Error downloading terraform files from document storage", e);
        }
    }

}
