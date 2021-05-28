package com.infra.resources.core.usecase.cluster;

import com.infra.resources.adapter.controller.cluster.dto.ClusterCreation;
import com.infra.resources.adapter.repository.ClusterRepository;
import com.infra.resources.core.domain.Cluster;
import com.infra.resources.core.exceptions.ClusterAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterCluster {

    private final ClusterRepository clusterRepository;

    public Cluster execute(ClusterCreation clusterCreation) {
        checkIfExists(clusterCreation);

        Cluster cluster = getClusterFromCreation(clusterCreation);
        clusterRepository.save(cluster);
        return cluster;
    }

    private Cluster getClusterFromCreation(ClusterCreation clusterCreation) {
        Cluster cluster = new Cluster();
        cluster.setName(clusterCreation.getName());
        cluster.setHost(clusterCreation.getHost());
        cluster.setCreatedBy(clusterCreation.getCreatedBy());

        return cluster;
    }

    private void checkIfExists(ClusterCreation creation) {
        clusterRepository.findById(creation.getName()).ifPresent(cluster -> {
            throw new ClusterAlreadyExistsException("cluster " + cluster.getName() + " already exists");
        });
    }
}
