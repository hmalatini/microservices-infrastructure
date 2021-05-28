package com.infra.resources.adapter.controller.networking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetryConfigDTO {

    public enum RetryOn {
        FIVE_XX, GATEWAY_ERROR, RESET, CONNECT_FAILURE, RETRIABLE_STATUS_CODES, RETRIABLE_HEADERS
    }

    private Integer attempts;
    private String perTryTimeout;
    private RetryOn retryOn;
}
