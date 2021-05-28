package com.infra.resources.adapter.filemanager;

import java.io.File;

public interface MicroserviceFileManager {
    File createDir(String path);
    void deleteDir(String path);
}
