package com.infra.resources.core.exceptions;

public class MicroserviceNotFoundException extends RuntimeException {
    public MicroserviceNotFoundException() {
        super();
    }
    public MicroserviceNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
