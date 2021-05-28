package com.infra.resources.core.usecase.cluster;

import com.infra.resources.adapter.repository.ClusterRepository;
import com.infra.resources.core.domain.Cluster;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllClusters {

    private final ClusterRepository clusterRepository;

    public List<Cluster> execute() {
        List<Cluster> clusterList = new ArrayList<>();
        clusterRepository.findAll()
                         .forEach(clusterList::add);
        return clusterList;
    }
}
