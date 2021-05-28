package com.infra.resources.core.exceptions;

public class GitHubRepositoryException extends RuntimeException {
    public GitHubRepositoryException() {
        super();
    }
    public GitHubRepositoryException(String errorMsg) { super(errorMsg); }
    public GitHubRepositoryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
