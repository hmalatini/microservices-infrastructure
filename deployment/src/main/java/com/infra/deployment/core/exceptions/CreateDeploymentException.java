package com.infra.deployment.core.exceptions;

public class CreateDeploymentException extends RuntimeException {
    public CreateDeploymentException(String msg) {
        super(msg);
    }

    public CreateDeploymentException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
