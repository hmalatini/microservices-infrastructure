package com.infra.resources.core.exceptions;

public class ClusterNotFoundException extends RuntimeException {
    public ClusterNotFoundException() {
        super();
    }
    public ClusterNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
