package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkingEndpointRepository extends
    CrudRepository<NetworkingEndpoint, Long> {

    List<NetworkingEndpoint> findAllByMicroserviceName(String microserviceName);

    List<NetworkingEndpoint> findAllByMicroserviceNameAndEnvironmentName(String microserviceName,
                                                                         String environmentName);

    Optional<NetworkingEndpoint> findByMicroserviceNameAndEnvironmentNameAndEndpointTypeAndName(String microserviceName,
                                                                                                String environmentName,
                                                                                                Type type, String name);
}
