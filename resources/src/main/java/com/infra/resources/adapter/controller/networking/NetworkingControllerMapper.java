package com.infra.resources.adapter.controller.networking;

import com.infra.resources.adapter.controller.networking.EndpointMatchValueDTO.MatchType;
import com.infra.resources.adapter.controller.networking.HeaderConfigValueDTO.Operation;
import com.infra.resources.adapter.controller.networking.LoadBalancerDTO.LoadBalancerType;
import com.infra.resources.core.domain.networking.Networking;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.RetryOn;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderRequestConfig;
import com.infra.resources.core.domain.networking.NetworkingEndpointHeaderResponseConfig;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchHeader;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchMethod;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchNoHeader;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchQueryParam;
import com.infra.resources.core.domain.networking.NetworkingEndpointMatchUri;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class NetworkingControllerMapper {


    public static NetworkingTrafficPolicyDTO mapTrafficPolicyToDto(NetworkingTrafficPolicy config) {
        NetworkingTrafficPolicyDTO.NetworkingTrafficPolicyDTOBuilder builder = NetworkingTrafficPolicyDTO.builder();
        if (Objects.isNull(config)) {
            return builder.build();
        }

        builder.name(config.getName())
               .environment(config.getEnvironmentName());
        if (Objects.nonNull(config.getCircuitBreakerConsecutiveGatewayErrors()) || Objects.nonNull(
            config.getCircuitBreakerConsecutive5xxErrors()) || Objects.nonNull(
            config.getCircuitBreakerBaseEjectionTime()) || Objects.nonNull(
            config.getCircuitBreakerInterval()) || Objects.nonNull(
            config.getCircuitBreakerMinHealthPercent()) || Objects.nonNull(
            config.getCircuitBreakerMaxEjectionPercent())) {
            builder.circuitBreaker(CircuitBreakerDTO.builder()
                                                    .consecutiveGatewayErrors(
                                                        config.getCircuitBreakerConsecutiveGatewayErrors())
                                                    .consecutive5xxErrors(
                                                        config.getCircuitBreakerConsecutive5xxErrors())
                                                    .interval(config.getCircuitBreakerInterval())
                                                    .baseEjectionTime(config.getCircuitBreakerBaseEjectionTime())
                                                    .maxEjectionPercent(config.getCircuitBreakerMaxEjectionPercent())
                                                    .minHealthPercent(config.getCircuitBreakerMinHealthPercent())
                                                    .build());
        }
        if (!Objects.isNull(config.getLoadBalancerType())) {
            builder.loadBalancer(LoadBalancerDTO.builder()
                                                .type(LoadBalancerType.valueOf(config.getLoadBalancerType()
                                                                                     .name()))
                                                .consistentHash(ConsistentHashConfigDTO.builder()
                                                                                       .minimumRingSize(
                                                                                           config.getLoadBalancerConsistentHashMinimumRingSize())
                                                                                       .match(
                                                                                           ConsistentHashMatchDTO.builder()
                                                                                                                 .headerName(
                                                                                                                     config.getLoadBalancerConsistentHashMatchHeaderName())
                                                                                                                 .queryParameterName(
                                                                                                                     config.getLoadBalancerConsistentHashMatchQueryParameterName())
                                                                                                                 .useSourceIp(
                                                                                                                     config.getLoadBalancerConsistentHashMatchUseSourceIp())
                                                                                                                 .cookie(
                                                                                                                     ConsistentHashMatchCookieDTO.builder()
                                                                                                                                                 .name(
                                                                                                                                                     config.getLoadBalancerConsistentHashMatchCookieName())
                                                                                                                                                 .path(
                                                                                                                                                     config.getLoadBalancerConsistentHashMatchCookiePath())
                                                                                                                                                 .ttl(
                                                                                                                                                     config.getLoadBalancerConsistentHashMatchCookieTtl())
                                                                                                                                                 .build())
                                                                                                                 .build())
                                                                                       .build())
                                                .build());
        }

        return builder.build();
    }

    public static EndpointDTO mapEndpointToDto(NetworkingEndpoint endpoint) {
        EndpointDTO.EndpointDTOBuilder builder = EndpointDTO.builder();
        if (Objects.isNull(endpoint)) {
            return builder.build();
        }

        return builder.match(EndpointMatchDTO.builder()
                                             .uri(getMatchUri(endpoint.getUri()))
                                             .method(getMatchMethod(endpoint.getMethod()))
                                             .headers(getMatchHeaders(endpoint.getHeaders()))
                                             .noHeaders(getMatchNoHeaders(endpoint.getNoHeaders()))
                                             .queryParams(getMatchQueryParams(endpoint.getQueryParams()))
                                             .build())
                      .redirect(endpoint.getRedirect())
                      .rewrite(endpoint.getRewrite())
                      .timeout(endpoint.getTimeout())
                      .retries(getRetries(endpoint.getRetryAttempts(), endpoint.getRetryPerTryTimeout(),
                          endpoint.getRetryOn()))
                      .faults(getFaults(endpoint.getFaultFixedDelay(), endpoint.getFaultDelayPercentage(),
                          endpoint.getFaultAbortHttpStatus(), endpoint.getFaultAbortPercentage()))
                      .headers(
                          getHeadersConfig(endpoint.getHeaderConfigRequests(), endpoint.getHeaderConfigResponses()))
                      .trafficPolicy(mapTrafficPolicyToDto(endpoint.getTrafficPolicy()))
                      .name(endpoint.getName())
                      .environment(endpoint.getEnvironmentName())
                      .build();

    }

    private static HeadersConfigDTO getHeadersConfig(List<NetworkingEndpointHeaderRequestConfig> headerConfigRequests,
                                                     List<NetworkingEndpointHeaderResponseConfig> headerConfigResponses) {

        HeadersConfigDTO.HeadersConfigDTOBuilder builder = HeadersConfigDTO.builder();

        if (!Objects.isNull(headerConfigRequests) && !headerConfigRequests.isEmpty()) {
            List<HeaderConfigValueDTO> list = new ArrayList<>();
            headerConfigRequests.forEach(header -> {
                HeaderConfigValueDTO headerConfig = HeaderConfigValueDTO.builder()
                                                                        .operation(Operation.valueOf(
                                                                            header.getOperation()
                                                                                  .name()))
                                                                        .headerName(header.getName())
                                                                        .headerValue(header.getValue())
                                                                        .build();
                list.add(headerConfig);
            });

            builder.request(list);
        }

        if (!Objects.isNull(headerConfigResponses) && !headerConfigResponses.isEmpty()) {
            List<HeaderConfigValueDTO> list = new ArrayList<>();
            headerConfigResponses.forEach(header -> {
                HeaderConfigValueDTO headerConfig = HeaderConfigValueDTO.builder()
                                                                        .operation(Operation.valueOf(
                                                                            header.getOperation()
                                                                                  .name()))
                                                                        .headerName(header.getName())
                                                                        .headerValue(header.getValue())
                                                                        .build();
                list.add(headerConfig);
            });

            builder.response(list);
        }

        return builder.build();
    }

    private static FaultConfigDTO getFaults(String faultFixedDelay, Integer faultDelayPercentage,
                                            Integer faultAbortHttpStatus, Integer faultAbortPercentage) {
        return FaultConfigDTO.builder()
                             .delay(DelayFaultDTO.builder()
                                                 .fixedDelay(faultFixedDelay)
                                                 .percentage(faultDelayPercentage)
                                                 .build())
                             .abort(AbortFaultDTO.builder()
                                                 .httpStatus(faultAbortHttpStatus)
                                                 .percentage(faultAbortPercentage)
                                                 .build())
                             .build();
    }

    private static RetryConfigDTO getRetries(Integer retryAttempts, String retryPerTryTimeout, RetryOn retryOn) {
        if (Objects.isNull(retryOn)) {
            return RetryConfigDTO.builder()
                                 .build();
        }

        return RetryConfigDTO.builder()
                             .attempts(retryAttempts)
                             .perTryTimeout(retryPerTryTimeout)
                             .retryOn(RetryConfigDTO.RetryOn.valueOf(retryOn.name()))
                             .build();
    }

    private static Map<String, EndpointMatchValueDTO> getMatchQueryParams(
        List<NetworkingEndpointMatchQueryParam> queryParams) {

        Map<String, EndpointMatchValueDTO> map = new HashMap<>();
        if (Objects.isNull(queryParams) || queryParams.isEmpty()) {
            return map;
        }

        queryParams.forEach(queryParam -> {
            EndpointMatchValueDTO valueDTO = EndpointMatchValueDTO.builder()
                                                                  .type(MatchType.valueOf(queryParam.getType()
                                                                                                    .name()))
                                                                  .value(queryParam.getValue())
                                                                  .build();
            map.put(queryParam.getName(), valueDTO);
        });

        return map;
    }


    private static Map<String, EndpointMatchValueDTO> getMatchNoHeaders(
        List<NetworkingEndpointMatchNoHeader> noHeaders) {

        Map<String, EndpointMatchValueDTO> map = new HashMap<>();
        if (Objects.isNull(noHeaders) || noHeaders.isEmpty()) {
            return map;
        }

        noHeaders.forEach(header -> {
            EndpointMatchValueDTO valueDTO = EndpointMatchValueDTO.builder()
                                                                  .type(MatchType.valueOf(header.getType()
                                                                                                .name()))
                                                                  .value(header.getValue())
                                                                  .build();
            map.put(header.getName(), valueDTO);
        });

        return map;
    }

    private static Map<String, EndpointMatchValueDTO> getMatchHeaders(List<NetworkingEndpointMatchHeader> headers) {
        Map<String, EndpointMatchValueDTO> map = new HashMap<>();
        if (Objects.isNull(headers) || headers.isEmpty()) {
            return map;
        }

        headers.forEach(header -> {
            EndpointMatchValueDTO valueDTO = EndpointMatchValueDTO.builder()
                                                                  .type(MatchType.valueOf(header.getType()
                                                                                                .name()))
                                                                  .value(header.getValue())
                                                                  .build();
            map.put(header.getName(), valueDTO);
        });

        return map;
    }

    private static EndpointMatchValueDTO getMatchMethod(NetworkingEndpointMatchMethod method) {
        EndpointMatchValueDTO.EndpointMatchValueDTOBuilder builder = EndpointMatchValueDTO.builder();
        if (Objects.isNull(method)) {
            return builder.build();
        }

        return builder.type(MatchType.valueOf(method.getType()
                                                    .name()))
                      .value(method.getValue())
                      .build();
    }

    private static EndpointMatchValueDTO getMatchUri(NetworkingEndpointMatchUri uri) {
        EndpointMatchValueDTO.EndpointMatchValueDTOBuilder builder = EndpointMatchValueDTO.builder();
        if (Objects.isNull(uri)) {
            return builder.build();
        }

        return builder.type(MatchType.valueOf(uri.getType()
                                                 .name()))
                      .value(uri.getValue())
                      .build();
    }

    public static NetworkingDTO mapNetworkingToDto(Networking networking) {
        NetworkingDTO.NetworkingDTOBuilder builder = NetworkingDTO.builder();
        if (Objects.isNull(networking)) {
            return builder.build();
        }

        return builder.trafficPolicies(mapTrafficPolicyListToDto(networking.getTrafficPolicies()))
                      .publicEndpoints(networking.getPublicEndpoints()
                                                 .stream()
                                                 .map(NetworkingControllerMapper::mapEndpointToDto)
                                                 .collect(
                                                     Collectors.toList()))
                      .privateEndpoints(networking.getPrivateEndpoints()
                                                  .stream()
                                                  .map(NetworkingControllerMapper::mapEndpointToDto)
                                                  .collect(
                                                      Collectors.toList()))
                      .build();
    }

    private static List<NetworkingTrafficPolicyDTO> mapTrafficPolicyListToDto(
        List<NetworkingTrafficPolicy> trafficPolicies) {
        if (Objects.isNull(trafficPolicies) || trafficPolicies.isEmpty()) {
            return Collections.emptyList();
        }

        return trafficPolicies.stream()
                              .map(NetworkingControllerMapper::mapTrafficPolicyToDto)
                              .collect(Collectors.toList());
    }
}
