package com.infra.deployment.adapter.gateway.networking;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.AbortFault;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.CircuitBreaker;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.ConsistentHashConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.ConsistentHashMatch;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.ConsistentHashMatchCookie;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.DelayFault;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.Endpoint;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.EndpointMatch;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.EndpointMatchValue;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.EndpointMatchValue.MatchType;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.FaultConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.HeaderConfigValue;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.HeadersConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.HeadersConfig.Operation;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.LoadBalancer;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.LoadBalancer.LoadBalancerType;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.Networking;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.NetworkingPrivateConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.NetworkingPublicConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.NetworkingTrafficPolicy;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.RetryConfig;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.networking.RetryConfig.RetryOn;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class NetworkingGatewayMapper {

    static Networking map(NetworkingDTO dto) {
        return Networking.builder()
                         .trafficPolicies(mapTrafficPolicies(dto.getTrafficPolicies()))
                         .publicConfig(mapPublicConfig(dto.getPublicEndpoints()))
                         .privateConfig(mapPrivateConfig(dto.getPrivateEndpoints()))
                         .build();
    }

    private static List<NetworkingTrafficPolicy> mapTrafficPolicies(
        List<NetworkingTrafficPolicyDTO> trafficPolicyList) {
        if (Objects.isNull(trafficPolicyList) || trafficPolicyList.isEmpty()) {
            return null;
        }

        return trafficPolicyList.stream()
                                .map(NetworkingGatewayMapper::mapTrafficPolicy)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
    }

    private static NetworkingTrafficPolicy mapTrafficPolicy(NetworkingTrafficPolicyDTO trafficPolicy) {
        if (Objects.isNull(trafficPolicy) || (Objects.isNull(trafficPolicy.getLoadBalancer()) && Objects.isNull(
            trafficPolicy.getCircuitBreaker()))) {
            return null;
        }

        return NetworkingTrafficPolicy.builder()
                                      .name(trafficPolicy.getName())
                                      .loadBalancer(mapLoadBalancer(trafficPolicy.getLoadBalancer()))
                                      .circuitBreaker(mapCircuitBreaker(trafficPolicy.getCircuitBreaker()))
                                      .build();
    }

    private static LoadBalancer mapLoadBalancer(LoadBalancerDTO loadBalancer) {
        if (Objects.isNull(loadBalancer) || Objects.isNull(loadBalancer.getType())) {
            return null;
        }

        return LoadBalancer.builder()
                           .type(LoadBalancerType.valueOf(loadBalancer.getType()
                                                                      .name()))
                           .consistentHash(mapLoadBalancerConsistentHash(loadBalancer.getConsistentHash()))
                           .build();
    }

    private static ConsistentHashConfig mapLoadBalancerConsistentHash(ConsistentHashConfigDTO consistentHash) {
        if (Objects.isNull(consistentHash)) {
            return null;
        }

        return ConsistentHashConfig.builder()
                                   .minimumRingSize(consistentHash.getMinimumRingSize())
                                   .match(mapLoadBalancerConsistentHashMatch(consistentHash.getMatch()))
                                   .build();
    }

    private static ConsistentHashMatch mapLoadBalancerConsistentHashMatch(ConsistentHashMatchDTO match) {
        if (Objects.isNull(match)) {
            return null;
        }

        return ConsistentHashMatch.builder()
                                  .headerName(match.getHeaderName())
                                  .queryParameterName(match.getQueryParameterName())
                                  .useSourceIp(match.getUseSourceIp())
                                  .cookie(mapLoadBalancerConsistentHashMatchCookie(match.getCookie()))
                                  .build();
    }

    private static ConsistentHashMatchCookie mapLoadBalancerConsistentHashMatchCookie(
        ConsistentHashMatchCookieDTO cookie) {
        if (Objects.isNull(cookie)) {
            return null;
        }

        return ConsistentHashMatchCookie.builder()
                                        .name(cookie.getName())
                                        .path(cookie.getPath())
                                        .ttl(cookie.getTtl())
                                        .build();
    }

    private static CircuitBreaker mapCircuitBreaker(CircuitBreakerDTO circuitBreaker) {
        if (Objects.isNull(circuitBreaker) || (Objects.isNull(circuitBreaker.getConsecutiveGatewayErrors()) &&
            Objects.isNull(circuitBreaker.getConsecutive5xxErrors()) && Objects.isNull(circuitBreaker.getInterval())
            && Objects.isNull(circuitBreaker.getBaseEjectionTime()) && Objects.isNull(
            circuitBreaker.getMaxEjectionPercent())
            && Objects.isNull(circuitBreaker.getMinHealthPercent()))) {
            return null;
        }

        return CircuitBreaker.builder()
                             .consecutiveGatewayErrors(circuitBreaker.getConsecutiveGatewayErrors())
                             .consecutive5xxErrors(circuitBreaker.getConsecutive5xxErrors())
                             .interval(circuitBreaker.getInterval())
                             .baseEjectionTime(circuitBreaker.getBaseEjectionTime())
                             .maxEjectionPercent(circuitBreaker.getMaxEjectionPercent())
                             .minHealthPercent(circuitBreaker.getMinHealthPercent())
                             .build();
    }

    private static NetworkingPublicConfig mapPublicConfig(List<EndpointDTO> endpoints) {
        if (Objects.isNull(endpoints) || endpoints.isEmpty()) {
            return null;
        }

        return NetworkingPublicConfig.builder()
                                     .endpoints(mapEndpoints(endpoints))
                                     .build();
    }

    private static NetworkingPrivateConfig mapPrivateConfig(List<EndpointDTO> endpoints) {
        if (Objects.isNull(endpoints) || endpoints.isEmpty()) {
            return null;
        }

        return NetworkingPrivateConfig.builder()
                                      .endpoints(mapEndpoints(endpoints))
                                      .build();
    }

    private static List<Endpoint> mapEndpoints(List<EndpointDTO> endpoints) {
        if (Objects.isNull(endpoints) || endpoints.isEmpty()) {
            return null;
        }

        return endpoints.stream()
                        .map(NetworkingGatewayMapper::mapEndpoint)
                        .collect(Collectors.toList());
    }

    private static Endpoint mapEndpoint(EndpointDTO endpointDTO) {
        if (Objects.isNull(endpointDTO)) {
            return null;
        }

        Endpoint.EndpointBuilder builder = Endpoint.builder()
                                                   .name(endpointDTO.getName())
                                                   .match(mapEndpointMatch(endpointDTO.getMatch()))
                                                   .redirect(endpointDTO.getRedirect())
                                                   .rewrite(endpointDTO.getRewrite())
                                                   .timeout(endpointDTO.getTimeout())
                                                   .retries(mapEndpointRetires(endpointDTO.getRetries()))
                                                   .faults(mapEndpointFaults(endpointDTO.getFaults()))
                                                   .headers(mapEndpointHeaders(endpointDTO.getHeaders()));

        if (!Objects.isNull(endpointDTO.getTrafficPolicy())) {
            builder.trafficPolicyName(endpointDTO.getTrafficPolicy()
                                                 .getName());
        }

        return builder.build();
    }

    private static EndpointMatch mapEndpointMatch(EndpointMatchDTO match) {
        if (Objects.isNull(match)) {
            return null;
        }

        return EndpointMatch.builder()
                            .uri(mapSimpleMatch(match.getUri()))
                            .method(mapSimpleMatch(match.getMethod()))
                            .headers(mapListOfMatch(match.getHeaders()))
                            .noHeaders(mapListOfMatch(match.getNoHeaders()))
                            .queryParams(mapListOfMatch(match.getQueryParams()))
                            .build();
    }

    private static Map<String, EndpointMatchValue> mapListOfMatch(Map<String, EndpointMatchValueDTO> matchMap) {
        if (Objects.isNull(matchMap) || matchMap.isEmpty()) {
            return null;
        }

        return matchMap.entrySet()
                       .stream()
                       .collect(Collectors.toMap(Entry::getKey, entry -> mapSimpleMatch(entry.getValue())));
    }

    private static EndpointMatchValue mapSimpleMatch(EndpointMatchValueDTO match) {
        if (Objects.isNull(match) || Objects.isNull(match.getType()) || Objects.isNull(match.getValue())) {
            return null;
        }

        return EndpointMatchValue.builder()
                                 .type(MatchType.valueOf(match.getType()
                                                              .name())
                                                .getType())
                                 .value(match.getValue())
                                 .build();
    }

    private static RetryConfig mapEndpointRetires(RetryConfigDTO retries) {
        if (Objects.isNull(retries) || (Objects.isNull(retries.getAttempts()) && Objects.isNull(
            retries.getPerTryTimeout()) && Objects.isNull(retries.getRetryOn()))) {
            return null;
        }

        RetryConfig.RetryConfigBuilder builder = RetryConfig.builder()
                                                            .attempts(retries.getAttempts())
                                                            .perTryTimeout(retries.getPerTryTimeout());

        if (!Objects.isNull(retries.getRetryOn())) {
            builder.retryOn(RetryOn.valueOf(retries.getRetryOn()
                                                   .name())
                                   .getRetryName());
        }

        return builder.build();
    }

    private static FaultConfig mapEndpointFaults(FaultConfigDTO faults) {
        if (Objects.isNull(faults) || (Objects.isNull(faults.getDelay()) && Objects.isNull(faults.getAbort()))) {
            return null;
        }

        DelayFault delayFault = mapEndpointFaultDelay(faults.getDelay());
        AbortFault abortFault = mapEndpointFaultAbort(faults.getAbort());

        if (Objects.isNull(delayFault) && Objects.isNull(abortFault)) {
            return null;
        }

        return FaultConfig.builder()
                          .delay(delayFault)
                          .abort(abortFault)
                          .build();
    }

    private static DelayFault mapEndpointFaultDelay(DelayFaultDTO delay) {
        if (Objects.isNull(delay) || (Objects.isNull(delay.getFixedDelay())) && Objects.isNull(delay.getPercentage())) {
            return null;
        }

        return DelayFault.builder()
                         .fixedDelay(delay.getFixedDelay())
                         .percentage(delay.getPercentage())
                         .build();
    }

    private static AbortFault mapEndpointFaultAbort(AbortFaultDTO abort) {
        if (Objects.isNull(abort) || (Objects.isNull(abort.getHttpStatus())) && Objects.isNull(abort.getPercentage())) {
            return null;
        }

        return AbortFault.builder()
                         .httpStatus(abort.getHttpStatus())
                         .percentage(abort.getPercentage())
                         .build();
    }

    private static HeadersConfig mapEndpointHeaders(HeadersConfigDTO headers) {
        if (Objects.isNull(headers) ||
            ((Objects.isNull(headers.getRequest()) || headers.getRequest()
                                                             .isEmpty()) &&
                (Objects.isNull(headers.getResponse()) || headers.getResponse()
                                                                 .isEmpty()))) {
            return null;
        }

        return HeadersConfig.builder()
                            .request(mapEndpointHeadersConfig(headers.getRequest()))
                            .response(mapEndpointHeadersConfig(headers.getResponse()))
                            .build();
    }

    private static Map<String, List<HeaderConfigValue>> mapEndpointHeadersConfig(
        List<HeaderConfigValueDTO> headerList) {
        if (Objects.isNull(headerList) || headerList.isEmpty()) {
            return null;
        }

        return headerList.stream()
                         .collect(
                             Collectors.groupingBy(
                                 NetworkingGatewayMapper::getHeaderConfigValueOperation,
                                 Collectors.mapping(NetworkingGatewayMapper::mapHeaderConfig, Collectors.toList())
                             )
                         );
    }

    private static String getHeaderConfigValueOperation(HeaderConfigValueDTO headerConfigDTO) {
        return Operation.valueOf(headerConfigDTO.getOperation()
                                                .name())
                        .getOperationName();
    }

    private static HeaderConfigValue mapHeaderConfig(HeaderConfigValueDTO headerConfigValueDTO) {
        if (Objects.isNull(headerConfigValueDTO)) {
            return null;
        }

        return HeaderConfigValue.builder()
                                .headerName(headerConfigValueDTO.getHeaderName())
                                .headerValue(headerConfigValueDTO.getHeaderValue())
                                .build();
    }
}
