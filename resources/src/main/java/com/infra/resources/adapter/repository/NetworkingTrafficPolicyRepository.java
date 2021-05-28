package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkingTrafficPolicyRepository extends
    CrudRepository<NetworkingTrafficPolicy, Long> {

    List<NetworkingTrafficPolicy> findAllByMicroserviceName(String microserviceName);

    List<NetworkingTrafficPolicy> findAllByMicroserviceNameAndEnvironmentName(String microserviceName,
                                                                              String environmentName);

    Optional<NetworkingTrafficPolicy> findByMicroserviceNameAndEnvironmentNameAndName(String microserviceName,
                                                                                      String environmentName,
                                                                                      String name);
}
