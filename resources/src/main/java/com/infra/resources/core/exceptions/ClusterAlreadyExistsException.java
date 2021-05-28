package com.infra.resources.core.exceptions;

public class ClusterAlreadyExistsException extends RuntimeException {
    public ClusterAlreadyExistsException(String msg) {
        super(msg);
    }
}
