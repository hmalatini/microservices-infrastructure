package com.infra.application.core.exceptions;

public class FileManagerException extends RuntimeException {
    public FileManagerException(String msg) {
        super(msg);
    }

    public FileManagerException(String msg, Throwable e) {
        super(msg, e);
    }
}
