package com.infra.resources.core.exceptions;

public class TrafficPolicyNotFoundException extends RuntimeException {
    public TrafficPolicyNotFoundException() {
        super();
    }
    public TrafficPolicyNotFoundException(String errorMsg) {
        super(errorMsg);
    }
    public TrafficPolicyNotFoundException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
