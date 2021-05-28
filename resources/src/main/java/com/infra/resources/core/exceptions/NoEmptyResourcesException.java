package com.infra.resources.core.exceptions;

public class NoEmptyResourcesException extends RuntimeException {
    public NoEmptyResourcesException(String errorMsg) {
        super(errorMsg);
    }
}
