package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.EndpointDTO;
import com.infra.resources.adapter.controller.networking.EndpointMatchDTO;
import com.infra.resources.adapter.controller.networking.FaultConfigDTO;
import com.infra.resources.adapter.controller.networking.HeaderConfigValueDTO.Operation;
import com.infra.resources.adapter.controller.networking.HeadersConfigDTO;
import com.infra.resources.adapter.controller.networking.RetryConfigDTO;
import com.infra.resources.core.domain.networking.HeaderConfigOperation;
import com.infra.resources.core.domain.networking.MatchType;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.NetworkingEndpointBuilder;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.RetryOn;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderRequestConfig;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderRequestConfig.NetworkingEndpointHeaderRequestConfigBuilder;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderResponseConfig;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderResponseConfig.NetworkingEndpointHeaderResponseConfigBuilder;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchHeader;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchMethod;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchNoHeader;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchQueryParam;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchUri;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
class EndpointDTOtoNetworkEndpoint {

    NetworkingEndpoint map(String microserviceName, String environmentName, String endpointName,
                           Type endpointType, EndpointDTO endpointDTO, NetworkingTrafficPolicy trafficPolicy,
                           Long endpointId) {
        NetworkingEndpointBuilder builder = NetworkingEndpoint.builder();
        if (Objects.nonNull(endpointId)) {
            builder.id(endpointId);
        }

        createMatchConfig(endpointDTO.getMatch(), builder);
        createRewriteAndRedirectConfig(endpointDTO.getRedirect(), endpointDTO.getRewrite(), builder);
        createTimeoutConfig(endpointDTO.getTimeout(), builder);
        createRetryConfig(endpointDTO.getRetries(), builder);
        createFaultConfig(endpointDTO.getFaults(), builder);
        createHeaderInjectionConfig(endpointDTO.getHeaders(), builder);
        createTrafficPolicy(trafficPolicy, builder);

        NetworkingEndpoint endpoint = builder.microserviceName(microserviceName)
                                             .environmentName(environmentName)
                                             .name(endpointName)
                                             .endpointType(endpointType)
                                             .build();

        mapChildren(endpoint);

        return endpoint;
    }

    private void mapChildren(NetworkingEndpoint endpoint) {
        if (Objects.nonNull(endpoint.getHeaders())) {
            endpoint.getHeaders()
                    .forEach(h -> h.setEndpoint(endpoint));
        }
        if (Objects.nonNull(endpoint.getNoHeaders())) {
            endpoint.getNoHeaders()
                    .forEach(h -> h.setEndpoint(endpoint));
        }
        if (Objects.nonNull(endpoint.getQueryParams())) {
            endpoint.getQueryParams()
                    .forEach(h -> h.setEndpoint(endpoint));
        }
        if (Objects.nonNull(endpoint.getHeaderConfigRequests())) {
            endpoint.getHeaderConfigRequests()
                    .forEach(h -> h.setEndpoint(endpoint));
        }
        if (Objects.nonNull(endpoint.getHeaderConfigResponses())) {
            endpoint.getHeaderConfigResponses()
                    .forEach(h -> h.setEndpoint(endpoint));
        }
    }

    private void createTrafficPolicy(NetworkingTrafficPolicy trafficPolicy, NetworkingEndpointBuilder builder) {
        if (Objects.nonNull(trafficPolicy)) {
            builder.trafficPolicy(trafficPolicy);
        }
    }

    private void createMatchConfig(EndpointMatchDTO match, NetworkingEndpointBuilder builder) {
        if (Objects.isNull(match)) {
            return;
        }

        if (Objects.nonNull(match.getUri()) && Objects.nonNull(match.getUri()
                                                                    .getType()) && Strings.isNotBlank(match.getUri()
                                                                                                           .getValue())) {
            builder.uri(NetworkingEndpointMatchUri.builder()
                                                  .type(MatchType.valueOf(match.getUri()
                                                                               .getType()
                                                                               .name()))
                                                  .value(match.getUri()
                                                              .getValue())
                                                  .build());
        }

        if (Objects.nonNull(match.getMethod()) && Objects.nonNull(match.getMethod()
                                                                       .getType()) && Strings.isNotBlank(
            match.getMethod()
                 .getValue())) {
            builder.method(NetworkingEndpointMatchMethod.builder()
                                                        .type(MatchType.valueOf(match.getMethod()
                                                                                     .getType()
                                                                                     .name()))
                                                        .value(match.getMethod()
                                                                    .getValue())
                                                        .build());
        }

        if (Objects.nonNull(match.getHeaders()) && !match.getHeaders()
                                                         .isEmpty()) {
            List<NetworkingEndpointMatchHeader> list = new ArrayList<>();
            match.getHeaders()
                 .forEach((name, matchValue) -> {
                         if (Strings.isNotBlank(name) && Objects.nonNull(matchValue) && Objects.nonNull(
                             matchValue.getType()) && Objects.nonNull(matchValue.getValue())) {
                             list.add(
                                 NetworkingEndpointMatchHeader.builder()
                                                              .name(name)
                                                              .type(MatchType.valueOf(matchValue.getType()
                                                                                                .name()))
                                                              .value(matchValue.getValue())
                                                              .build()
                             );
                         }
                     }
                 );
            builder.headers(list);
        }

        if (Objects.nonNull(match.getNoHeaders()) && !match.getNoHeaders()
                                                           .isEmpty()) {
            List<NetworkingEndpointMatchNoHeader> list = new ArrayList<>();
            match.getQueryParams()
                 .forEach((name, matchValue) -> {
                         if (Strings.isNotBlank(name) && Objects.nonNull(matchValue) && Objects.nonNull(
                             matchValue.getType()) && Objects.nonNull(matchValue.getValue())) {
                             list.add(
                                 NetworkingEndpointMatchNoHeader.builder()
                                                                .name(name)
                                                                .type(MatchType.valueOf(matchValue.getType()
                                                                                                  .name()))
                                                                .value(matchValue.getValue())
                                                                .build()
                             );
                         }
                     }
                 );
            builder.noHeaders(list);
        }

        if (Objects.nonNull(match.getQueryParams()) && !match.getQueryParams()
                                                             .isEmpty()) {
            List<NetworkingEndpointMatchQueryParam> list = new ArrayList<>();
            match.getQueryParams()
                 .forEach((name, matchValue) -> {
                         if (Strings.isNotBlank(name) && Objects.nonNull(matchValue) && Objects.nonNull(
                             matchValue.getType()) && Objects.nonNull(matchValue.getValue())) {
                             list.add(
                                 NetworkingEndpointMatchQueryParam.builder()
                                                                  .name(name)
                                                                  .type(MatchType.valueOf(matchValue.getType()
                                                                                                    .name()))
                                                                  .value(matchValue.getValue())
                                                                  .build()
                             );
                         }
                     }
                 );
            builder.queryParams(list);
        }
    }

    private void createRewriteAndRedirectConfig(String redirect, String rewrite,
                                                NetworkingEndpointBuilder builder) {
        if (Strings.isNotBlank(redirect)) {
            builder.redirect(redirect);
        } else if (Strings.isNotBlank(rewrite)) {
            builder.rewrite(rewrite);
        }
    }

    private void createTimeoutConfig(String timeout, NetworkingEndpointBuilder builder) {
        if (Strings.isNotBlank(timeout)) {
            builder.timeout(timeout);
        }
    }

    private void createRetryConfig(RetryConfigDTO retries, NetworkingEndpointBuilder builder) {
        if (Objects.isNull(retries)) {
            return;
        }

        if (Objects.nonNull(retries.getAttempts()) && Strings.isNotBlank(retries.getPerTryTimeout()) && Objects.nonNull(
            retries.getRetryOn())) {
            builder.retryAttempts(retries.getAttempts())
                   .retryPerTryTimeout(retries.getPerTryTimeout())
                   .retryOn(RetryOn.valueOf(retries.getRetryOn()
                                                   .name()));
        }
    }

    private void createFaultConfig(FaultConfigDTO faults, NetworkingEndpointBuilder builder) {
        if (Objects.isNull(faults)) {
            return;
        }

        if (Objects.nonNull(faults.getDelay()) && Strings.isNotBlank(faults.getDelay()
                                                                           .getFixedDelay()) && Objects.nonNull(
            faults.getDelay()
                  .getPercentage())) {
            builder.faultDelayPercentage(faults.getDelay()
                                               .getPercentage())
                   .faultFixedDelay(faults.getDelay()
                                          .getFixedDelay());
        }

        if (Objects.nonNull(faults.getAbort()) && Objects.nonNull(faults.getAbort()
                                                                        .getHttpStatus()) && Objects.nonNull(
            faults.getAbort()
                  .getPercentage())) {
            builder.faultAbortPercentage(faults.getAbort()
                                               .getPercentage())
                   .faultAbortHttpStatus(faults.getAbort()
                                               .getHttpStatus());
        }
    }

    private void createHeaderInjectionConfig(HeadersConfigDTO headers,
                                             NetworkingEndpointBuilder builder) {
        if (Objects.isNull(headers)) {
            return;
        }

        if (Objects.nonNull(headers.getRequest()) && !headers.getRequest()
                                                             .isEmpty()) {
            List<NetworkingEndpointHeaderRequestConfig> list = new ArrayList<>();
            headers.getRequest()
                   .forEach(header -> {
                       if (Objects.nonNull(header.getOperation()) && Strings.isNotBlank(header.getHeaderName())) {
                           NetworkingEndpointHeaderRequestConfigBuilder headerBuilder = NetworkingEndpointHeaderRequestConfig.builder()
                                                                                                                             .name(
                                                                                                                                 header.getHeaderName())
                                                                                                                             .operation(
                                                                                                                                 HeaderConfigOperation.valueOf(
                                                                                                                                     header.getOperation()
                                                                                                                                           .name()));

                           if (header.getOperation()
                                     .equals(Operation.REMOVE)) {
                               list.add(headerBuilder.build());
                           } else if (Strings.isNotBlank(header.getHeaderValue())) {
                               headerBuilder.value(header.getHeaderValue());
                               list.add(headerBuilder.build());
                           }
                       }
                   });

            builder.headerConfigRequests(list);
        }

        if (Objects.nonNull(headers.getResponse()) && !headers.getResponse()
                                                              .isEmpty()) {
            List<NetworkingEndpointHeaderResponseConfig> list = new ArrayList<>();
            headers.getResponse()
                   .forEach(header -> {
                       if (Objects.nonNull(header.getOperation()) && Strings.isNotBlank(header.getHeaderName())) {
                           NetworkingEndpointHeaderResponseConfigBuilder headerBuilder = NetworkingEndpointHeaderResponseConfig.builder()
                                                                                                                               .name(
                                                                                                                                   header.getHeaderName())
                                                                                                                               .operation(
                                                                                                                                   HeaderConfigOperation.valueOf(
                                                                                                                                       header.getOperation()
                                                                                                                                             .name()));

                           if (header.getOperation()
                                     .equals(Operation.REMOVE)) {
                               list.add(headerBuilder.build());
                           } else if (Strings.isNotBlank(header.getHeaderValue())) {
                               headerBuilder.value(header.getHeaderValue());
                               list.add(headerBuilder.build());
                           }
                       }
                   });

            builder.headerConfigResponses(list);
        }
    }

}
