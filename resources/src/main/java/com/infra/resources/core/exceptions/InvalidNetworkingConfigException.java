package com.infra.resources.core.exceptions;

public class InvalidNetworkingConfigException extends RuntimeException {
    public InvalidNetworkingConfigException() {
        super();
    }
    public InvalidNetworkingConfigException(String errorMsg) {
        super(errorMsg);
    }
    public InvalidNetworkingConfigException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
