package com.infra.resources.core.domain.networking;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "networking_endpoint",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"microservice_name", "environment_name", "name",
        "endpoint_type"}, name = "unique_endpoint_name")}
)
public class NetworkingEndpoint {

    public enum RetryOn {
        FIVE_XX, GATEWAY_ERROR, RESET, CONNECT_FAILURE, RETRIABLE_STATUS_CODES, RETRIABLE_HEADERS
    }

    public enum Type {
        PUBLIC, PRIVATE
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

    @Column(name = "endpoint_type")
    @Enumerated(EnumType.STRING)
    private Type endpointType;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "match_uri_id")
    private NetworkingEndpointMatchUri uri;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "match_method_id")
    private NetworkingEndpointMatchMethod method;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<NetworkingEndpointMatchHeader> headers;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<NetworkingEndpointMatchNoHeader> noHeaders;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<NetworkingEndpointMatchQueryParam> queryParams;

    private String rewrite;
    private String redirect;
    private String timeout;
    private Integer retryAttempts;
    private String retryPerTryTimeout;


    @Enumerated(EnumType.STRING)
    private RetryOn retryOn;

    private Integer faultDelayPercentage;
    private String faultFixedDelay;
    private Integer faultAbortHttpStatus;
    private Integer faultAbortPercentage;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<NetworkingEndpointHeaderRequestConfig> headerConfigRequests;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<NetworkingEndpointHeaderResponseConfig> headerConfigResponses;

    @ManyToOne
    @JoinColumn(name = "traffic_policy_id")
    private NetworkingTrafficPolicy trafficPolicy;
}
