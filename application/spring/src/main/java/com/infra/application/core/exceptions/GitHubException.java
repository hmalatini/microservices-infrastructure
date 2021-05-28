package com.infra.application.core.exceptions;

public class GitHubException extends RuntimeException {
    public GitHubException(String msg) {
        super(msg);
    }

    public GitHubException(String msg, Throwable e) {
        super(msg, e);
    }
}
