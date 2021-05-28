package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.domain.Secret.SecretId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends CrudRepository<Secret, SecretId> {

    List<Secret> findAllByMicroserviceName(String microserviceName);
}
