package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.Microservice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicroserviceRepository extends CrudRepository<Microservice, String> {
}
