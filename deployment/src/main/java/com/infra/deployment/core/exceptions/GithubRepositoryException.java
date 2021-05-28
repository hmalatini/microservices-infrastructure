package com.infra.deployment.core.exceptions;

public class GithubRepositoryException extends RuntimeException {

    public GithubRepositoryException(String msg) {
        super(msg);
    }
    public GithubRepositoryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
