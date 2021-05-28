package com.infra.resources.core.domain.networking;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "networking_traffic_policy",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"microservice_name", "environment_name",
        "name"}, name = "unique_traffic_policy_name")}
)
public class NetworkingTrafficPolicy {

    public enum LoadBalancerType {
        ROUND_ROBIN, LEAST_CONN, RANDOM, CONSISTENT_HASH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "microservice_name")
    private String microserviceName;
    @Column(name = "environment_name")
    private String environmentName;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    private LoadBalancerType loadBalancerType;
    private Long loadBalancerConsistentHashMinimumRingSize;
    private String loadBalancerConsistentHashMatchHeaderName;
    private String loadBalancerConsistentHashMatchQueryParameterName;
    private Boolean loadBalancerConsistentHashMatchUseSourceIp;
    private String loadBalancerConsistentHashMatchCookieName;
    private String loadBalancerConsistentHashMatchCookiePath;
    private String loadBalancerConsistentHashMatchCookieTtl;
    private Integer circuitBreakerConsecutiveGatewayErrors;
    private Integer circuitBreakerConsecutive5xxErrors;
    private String circuitBreakerInterval;
    private String circuitBreakerBaseEjectionTime;
    private Integer circuitBreakerMaxEjectionPercent;
    private Integer circuitBreakerMinHealthPercent;

    @OneToMany(mappedBy = "trafficPolicy")
    private List<NetworkingEndpoint> endpoints;
}
