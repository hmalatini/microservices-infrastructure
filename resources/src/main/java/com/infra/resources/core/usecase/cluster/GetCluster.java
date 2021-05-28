package com.infra.resources.core.usecase.cluster;

import com.infra.resources.adapter.repository.ClusterRepository;
import com.infra.resources.core.domain.Cluster;
import com.infra.resources.core.exceptions.ClusterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCluster {

    private final ClusterRepository clusterRepository;

    public Cluster execute(String clusterName) {
        return clusterRepository.findById(clusterName).orElseThrow(ClusterNotFoundException::new);
    }
}
