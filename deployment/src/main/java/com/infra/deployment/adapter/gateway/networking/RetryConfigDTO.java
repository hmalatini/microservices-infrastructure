package com.infra.deployment.adapter.gateway.networking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RetryConfigDTO {

    public enum RetryOn {
        FIVE_XX, GATEWAY_ERROR, RESET, CONNECT_FAILURE, RETRIABLE_STATUS_CODES, RETRIABLE_HEADERS
    }

    private Integer attempts;
    private String perTryTimeout;
    private RetryOn retryOn;
}
