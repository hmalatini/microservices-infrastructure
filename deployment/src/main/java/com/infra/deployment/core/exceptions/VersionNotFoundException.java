package com.infra.deployment.core.exceptions;

public class VersionNotFoundException extends RuntimeException {
    public VersionNotFoundException() {
        super();
    }
    public VersionNotFoundException(String msg) {
        super(msg);
    }
    public VersionNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
