package com.infra.resources.core.usecase.networking;

import com.infra.resources.adapter.controller.networking.CircuitBreakerDTO;
import com.infra.resources.adapter.controller.networking.ConsistentHashConfigDTO;
import com.infra.resources.adapter.controller.networking.ConsistentHashMatchDTO;
import com.infra.resources.adapter.controller.networking.LoadBalancerDTO;
import com.infra.resources.adapter.controller.networking.NetworkingTrafficPolicyDTO;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy.NetworkingTrafficPolicyBuilder;
import com.infra.resources.core.exceptions.InvalidNetworkingConfigException;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
class TrafficPolictyDTOtoNetworkTrafficPolicy {

    NetworkingTrafficPolicy map(String microserviceName, String environmentName, String name,
                                NetworkingTrafficPolicyDTO config, Long trafficPolicyId) {

        NetworkingTrafficPolicyBuilder builder = NetworkingTrafficPolicy.builder();
        if (Objects.nonNull(trafficPolicyId)) {
            builder.id(trafficPolicyId);
        }

        createLoadBalancingConfig(config.getLoadBalancer(), builder);
        createCircuitBreakingConfig(config.getCircuitBreaker(), builder);

        return builder.microserviceName(microserviceName)
                      .environmentName(environmentName)
                      .name(name)
                      .build();
    }

    private void createCircuitBreakingConfig(CircuitBreakerDTO config, NetworkingTrafficPolicyBuilder builder) {

        if (Objects.isNull(config)) {
            return;
        }

        builder.circuitBreakerConsecutiveGatewayErrors(config.getConsecutiveGatewayErrors())
               .circuitBreakerConsecutive5xxErrors(config.getConsecutive5xxErrors())
               .circuitBreakerInterval(config.getInterval())
               .circuitBreakerBaseEjectionTime(config.getBaseEjectionTime())
               .circuitBreakerMaxEjectionPercent(config.getMaxEjectionPercent())
               .circuitBreakerMinHealthPercent(config.getMinHealthPercent());
    }

    private void createLoadBalancingConfig(LoadBalancerDTO config, NetworkingTrafficPolicyBuilder builder) {

        if (Objects.isNull(config) || Objects.isNull(config.getType())) {
            return;
        }

        if (config.getType()
                  .equals(LoadBalancerDTO.LoadBalancerType.CONSISTENT_HASH)) {
            createConsistentHashLoadBalancerConfig(config.getConsistentHash(), builder);
        }

        builder.loadBalancerType(NetworkingTrafficPolicy.LoadBalancerType.valueOf(config.getType()
                                                                                        .name()));
    }

    private void createConsistentHashLoadBalancerConfig(ConsistentHashConfigDTO config,
                                                        NetworkingTrafficPolicyBuilder builder) {

        if (Objects.isNull(config)) {
            return;
        }

        builder = builder.loadBalancerConsistentHashMinimumRingSize(
            config.getMinimumRingSize());

        if (Objects.isNull(config.getMatch())) {
            throw new InvalidNetworkingConfigException(
                "No configuration provided for consistent hashing load balancer type");
        }

        ConsistentHashMatchDTO matchConfig = config.getMatch();

        if (!Objects.isNull(matchConfig.getHeaderName())) {
            builder.loadBalancerConsistentHashMatchHeaderName(
                matchConfig.getHeaderName());
        } else if (!Objects.isNull(matchConfig.getQueryParameterName())) {
            builder.loadBalancerConsistentHashMatchQueryParameterName(
                matchConfig.getQueryParameterName());
        } else if (!Objects.isNull(matchConfig.getUseSourceIp())) {
            builder.loadBalancerConsistentHashMatchUseSourceIp(
                matchConfig.getUseSourceIp());
        } else if (!Objects.isNull(matchConfig.getCookie()) && !Objects.isNull(matchConfig.getCookie()
                                                                                          .getName())
            && !Objects.isNull(
            matchConfig.getCookie()
                       .getPath())) {
            builder.loadBalancerConsistentHashMatchCookieName(matchConfig.getCookie()
                                                                         .getName())
                   .loadBalancerConsistentHashMatchCookiePath(matchConfig.getCookie()
                                                                         .getPath())
                   .loadBalancerConsistentHashMatchCookieTtl(matchConfig.getCookie()
                                                                        .getTtl());
        } else {
            throw new InvalidNetworkingConfigException(
                "No configuration provided for consistent hashing load balancer type");
        }
    }
}
