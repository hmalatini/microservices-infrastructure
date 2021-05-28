package com.infra.resources.core.exceptions;

public class EndpointNotFoundException extends RuntimeException {
    public EndpointNotFoundException() {
        super();
    }
    public EndpointNotFoundException(String errorMsg) {
        super(errorMsg);
    }
    public EndpointNotFoundException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
