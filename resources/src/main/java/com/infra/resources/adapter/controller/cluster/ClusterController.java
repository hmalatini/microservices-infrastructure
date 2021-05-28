package com.infra.resources.adapter.controller.cluster;

import com.infra.resources.adapter.controller.cluster.dto.ClusterCreation;
import com.infra.resources.core.domain.Cluster;
import com.infra.resources.core.usecase.cluster.GetAllClusters;
import com.infra.resources.core.usecase.cluster.RegisterCluster;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/cluster")
@RequiredArgsConstructor
public class ClusterController {

    private final RegisterCluster registerCluster;
    private final GetAllClusters getAllClusters;

    @PostMapping
    public ResponseEntity<Cluster> create(@Validated @RequestBody ClusterCreation clusterCreation) {

        Cluster c = registerCluster.execute(clusterCreation);

        return ResponseEntity.ok()
                             .body(c);
    }

    @GetMapping
    public ResponseEntity<List<Cluster>> getAll() {

        List<Cluster> c = getAllClusters.execute();

        return ResponseEntity.ok()
                             .body(c);
    }
}
