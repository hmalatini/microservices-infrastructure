package com.infra.deployment.adapter.controller;

import com.infra.deployment.core.domain.Deployment;
import com.infra.deployment.core.usecase.CreateDeployment;
import com.infra.deployment.core.usecase.UpdateConfigWithDeployment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/deployment")
@RequiredArgsConstructor
public class DeploymentController {

    private final CreateDeployment createDeployment;
    private final UpdateConfigWithDeployment updateConfigWithDeployment;

    @PostMapping
    public ResponseEntity<Deployment> create(@Validated @RequestBody Deployment deploymentCreation) {

        createDeployment.execute(deploymentCreation, getDeploymentDescpription(deploymentCreation));

        return ResponseEntity.ok()
                             .build();
    }

    @PutMapping
    public ResponseEntity<Deployment> updateConfig(@RequestParam String microserviceName,
                                                   @RequestParam String environmentName,
                                                   @Validated @RequestBody UpdateConfigDTO updateConfig) {

        updateConfigWithDeployment.execute(microserviceName, environmentName, updateConfig.getDescription());

        return ResponseEntity.ok()
                             .build();
    }

    private String getDeploymentDescpription(Deployment deployment) {
        return "deploy: version " + deployment.getVersion() + " - env " + deployment.getEnvironmentName();
    }
}
