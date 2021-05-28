package com.infra.resources.adapter.controller.microservice;

import com.infra.resources.adapter.controller.microservice.dto.MicroserviceCreation;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.usecase.microservice.CreateMicroservice;
import com.infra.resources.core.usecase.microservice.DestroyMicroservice;
import com.infra.resources.core.usecase.microservice.GetAllMicroservices;
import com.infra.resources.core.usecase.microservice.GetMicroservice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("backend/microservice")
@RequiredArgsConstructor
public class MicroservicesController {

    private final CreateMicroservice createMicroservice;
    private final DestroyMicroservice destroyMicroservice;
    private final GetAllMicroservices getAllMicroservices;
    private final GetMicroservice getMicroservice;

    @PostMapping
    public ResponseEntity<Microservice> create(@Validated @RequestBody MicroserviceCreation microserviceCreation) {

        Microservice m = createMicroservice.execute(microserviceCreation);

        return ResponseEntity.ok().body(m);
    }

    @DeleteMapping("/{microserviceName}")
    public ResponseEntity<Microservice> destroy(@PathVariable String microserviceName) {

        Microservice m = destroyMicroservice.execute(microserviceName);

        return ResponseEntity.ok().body(m);
    }

    @GetMapping("/{microserviceName}")
    public ResponseEntity<Microservice> retrieve(@PathVariable String microserviceName) {

        Microservice m = getMicroservice.execute(microserviceName);

        return ResponseEntity.ok().body(m);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Microservice>> retrieve() {
        List<Microservice> m = getAllMicroservices.execute();
        return ResponseEntity.ok().body(m);
    }
}
