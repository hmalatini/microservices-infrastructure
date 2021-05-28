package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.Environment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentRepository extends CrudRepository<Environment, Environment.EnvironmentId> {
    List<Environment> findAllByMicroserviceName(String microserviceName);
}
