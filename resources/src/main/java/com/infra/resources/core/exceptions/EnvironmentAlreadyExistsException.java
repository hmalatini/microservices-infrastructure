package com.infra.resources.core.exceptions;

public class EnvironmentAlreadyExistsException extends RuntimeException {
    public EnvironmentAlreadyExistsException(String msg) {
        super(msg);
    }
}
