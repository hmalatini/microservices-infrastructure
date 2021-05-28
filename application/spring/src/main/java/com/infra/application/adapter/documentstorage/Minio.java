package com.infra.application.adapter.documentstorage;

import com.infra.application.config.DocumentStorageConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.resourcemanager.DocumentStorageFromDeployment;
import com.infra.resources.adapter.documentstorage.DocumentStorageFromMicroservices;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.infra.application.config.Profiles.DEV;

@Component
@Profile({DEV})
public class Minio implements DocumentStorageFromDeployment, DocumentStorageFromMicroservices {

    private final MinioClient client;

    public Minio(DocumentStorageConfig documentStorageConfig) {
        this.client = MinioClient.builder()
                .endpoint(documentStorageConfig.getUrl())
                .credentials(documentStorageConfig.getUser(), documentStorageConfig.getPass())
                .build();
    }

    public List<String> listFilesInPath(String bucket, String documentStorageSrcPath) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results = client.listObjects(
                ListObjectsArgs.builder().bucket(bucket).startAfter(documentStorageSrcPath).recursive(true).build());

        Iterator<Result<Item>> resultIterator = results.iterator();
        if (!resultIterator.hasNext()) {
            return Collections.emptyList();
        }

        List<String> files = new ArrayList<>();
        while (resultIterator.hasNext()) {
            Result<Item> currentFile = resultIterator.next();
            files.add(
                    currentFile.get().objectName()
            );
        }

        return files;
    }

    public void downloadFile(String bucket, String documentStorageSrcPath, String dstPath) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try (InputStream stream = client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(documentStorageSrcPath)
                        .build())) {
            File dstFile = new File(dstPath);
            FileUtils.copyInputStreamToFile(stream, dstFile);
        }
    }
}
