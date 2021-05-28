package com.infra.resources.core.exceptions;

public class CreateResourceException extends RuntimeException {
    public CreateResourceException() {
        super();
    }
    public CreateResourceException(String errorMsg) {
        super(errorMsg);
    }
    public CreateResourceException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
