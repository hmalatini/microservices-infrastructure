package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.Cluster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterRepository extends CrudRepository<Cluster, String> {
}
