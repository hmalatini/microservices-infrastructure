package com.infra.deployment.core.exceptions;

public class HelmDeploymentCreatorException extends RuntimeException {
    public HelmDeploymentCreatorException(String msg) {
        super(msg);
    }
    public HelmDeploymentCreatorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
