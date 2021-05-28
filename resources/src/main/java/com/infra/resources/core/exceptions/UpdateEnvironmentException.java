package com.infra.resources.core.exceptions;

public class UpdateEnvironmentException extends RuntimeException {
    public UpdateEnvironmentException() {
        super();
    }
    public UpdateEnvironmentException(String errorMsg) {
        super(errorMsg);
    }
    public UpdateEnvironmentException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
