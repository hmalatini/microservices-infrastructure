package com.infra.resources.adapter.controller.environment;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentCreation;
import com.infra.resources.adapter.controller.environment.dto.EnvironmentUpdate;
import com.infra.resources.core.domain.Environment;
import com.infra.resources.core.usecase.environment.CreateEnvironment;
import com.infra.resources.core.usecase.environment.DestroyEnvironment;
import com.infra.resources.core.usecase.environment.GetAllEnvironments;
import com.infra.resources.core.usecase.environment.GetEnvironment;
import com.infra.resources.core.usecase.environment.UpdateEnvironment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/{microserviceName}/environment")
@RequiredArgsConstructor
public class EnvironmentController {

    private final CreateEnvironment createEnvironment;
    private final GetAllEnvironments getAllEnvironments;
    private final GetEnvironment getEnvironment;
    private final UpdateEnvironment updateEnvironment;
    private final DestroyEnvironment destroyEnvironment;

    @PostMapping
    public ResponseEntity<Environment> create(@Validated @RequestBody EnvironmentCreation environmentCreation,
                                              @PathVariable String microserviceName) {

        Environment e = createEnvironment.execute(environmentCreation, microserviceName);

        return ResponseEntity.ok()
                             .body(e);
    }

    @GetMapping
    public ResponseEntity<List<Environment>> getAll(@PathVariable String microserviceName) {

        List<Environment> e = getAllEnvironments.execute(microserviceName);

        return ResponseEntity.ok()
                             .body(e);
    }

    @GetMapping("/{envName}")
    public ResponseEntity<Environment> get(@PathVariable String microserviceName, @PathVariable String envName) {

        Environment e = getEnvironment.execute(microserviceName, envName);

        return ResponseEntity.ok()
                             .body(e);
    }

    @PutMapping("/{envName}")
    public ResponseEntity<Environment> update(@PathVariable String microserviceName, @PathVariable String envName, @Validated @RequestBody
        EnvironmentUpdate environmentUpdate) {

        Environment e = updateEnvironment.execute(microserviceName, envName, environmentUpdate);

        return ResponseEntity.ok()
                             .body(e);
    }

    @DeleteMapping("/{envName}")
    public ResponseEntity<Environment> destroy(@PathVariable String microserviceName, @PathVariable String envName) {

        Environment e = destroyEnvironment.execute(microserviceName, envName);

        return ResponseEntity.ok()
                             .body(e);
    }
}
