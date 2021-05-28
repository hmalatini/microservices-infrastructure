package com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RetryConfig {

    public enum RetryOn {
        FIVE_XX("5xx"),
        GATEWAY_ERROR("gateway-error"),
        RESET("reset"),
        CONNECT_FAILURE("connect-failure"),
        RETRIABLE_STATUS_CODES("retriable-status-codes"),
        RETRIABLE_HEADERS("retriable-headers");


        private final String retryName;

        RetryOn(String retryName) {
            this.retryName = retryName;
        }

        public String getRetryName() { return this.retryName; }
    }

    private Integer attempts;
    private String perTryTimeout;
    private String retryOn;
}
