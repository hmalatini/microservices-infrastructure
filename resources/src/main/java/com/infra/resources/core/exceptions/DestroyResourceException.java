package com.infra.resources.core.exceptions;

public class DestroyResourceException extends RuntimeException {
    public DestroyResourceException(String msg) {
        super(msg);
    }
    public DestroyResourceException(String msg, Throwable e) {
        super(msg, e);
    }
}
