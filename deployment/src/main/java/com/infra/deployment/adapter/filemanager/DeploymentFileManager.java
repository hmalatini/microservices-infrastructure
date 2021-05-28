package com.infra.deployment.adapter.filemanager;

import java.io.File;

public interface DeploymentFileManager {
    File createDir(String path);
    void deleteDir(String path);
}
