package com.infra.resources.core.exceptions;

public class SecretNotFoundException extends RuntimeException {
    public SecretNotFoundException() {
        super();
    }
    public SecretNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
