package com.infra.resources.core.exceptions;

public class MicroserviceAlreadyExistsException extends RuntimeException {
    public MicroserviceAlreadyExistsException(String msg) {
        super(msg);
    }
}
