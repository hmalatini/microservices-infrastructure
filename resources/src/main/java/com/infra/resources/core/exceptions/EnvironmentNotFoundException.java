package com.infra.resources.core.exceptions;

public class EnvironmentNotFoundException extends RuntimeException {
    public EnvironmentNotFoundException() {
        super();
    }

    public EnvironmentNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
