package com.infra.resources.adapter.documentstorage;

import java.util.List;

public interface DocumentStorageFromMicroservices {
    List<String> listFilesInPath(String bucket, String documentStorageSrcPath) throws Exception;

    void downloadFile(String bucket, String documentStorageSrcPath, String dstPath) throws Exception;
}
