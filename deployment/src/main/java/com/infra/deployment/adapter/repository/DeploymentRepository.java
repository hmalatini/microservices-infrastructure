package com.infra.deployment.adapter.repository;

import com.infra.deployment.core.domain.Deployment;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeploymentRepository extends CrudRepository<Deployment, Deployment.DeploymentId> {

    Optional<Deployment> findByMicroserviceNameAndEnvironmentName(String microserviceName, String environmentName);
}
