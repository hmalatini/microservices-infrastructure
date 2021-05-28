package com.infra.resources.core.exceptions;

public class SecretAlreadyExistsException extends RuntimeException {
    public SecretAlreadyExistsException(String msg) {
        super(msg);
    }
}
