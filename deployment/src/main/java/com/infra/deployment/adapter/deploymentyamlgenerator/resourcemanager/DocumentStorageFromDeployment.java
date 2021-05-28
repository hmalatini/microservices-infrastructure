package com.infra.deployment.adapter.deploymentyamlgenerator.resourcemanager;

import java.util.List;

public interface DocumentStorageFromDeployment {
    List<String> listFilesInPath(String bucket, String documentStorageSrcPath) throws Exception;

    void downloadFile(String bucket, String documentStorageSrcPath, String dstPath) throws Exception;
}
