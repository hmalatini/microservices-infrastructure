package com.infra.application.adapter.filemanager;

import com.infra.application.core.exceptions.FileManagerException;
import com.infra.deployment.adapter.filemanager.DeploymentFileManager;
import com.infra.resources.adapter.filemanager.MicroserviceFileManager;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileManager implements DeploymentFileManager, MicroserviceFileManager {

    public File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new FileManagerException("directory can not be created");
            }
        }

        return dir;
    }

    public void deleteDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                throw new FileManagerException("error deleting repo directory", e);
            }
        }
    }

}
