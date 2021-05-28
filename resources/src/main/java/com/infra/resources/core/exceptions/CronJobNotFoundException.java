package com.infra.resources.core.exceptions;

public class CronJobNotFoundException extends RuntimeException {
    public CronJobNotFoundException() {
        super();
    }
    public CronJobNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
