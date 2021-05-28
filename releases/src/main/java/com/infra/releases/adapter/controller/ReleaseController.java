package com.infra.releases.adapter.controller;

import com.infra.releases.core.domain.Release;
import com.infra.releases.core.usecase.CreateRelease;
import com.infra.releases.core.usecase.GetAllReleasesOfService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend")
@RequiredArgsConstructor
public class ReleaseController {

    private final CreateRelease createRelease;
    private final GetAllReleasesOfService getAllReleasesOfService;

    @PostMapping("{microserviceName}/releases")
    public ResponseEntity<Release> create(@PathVariable String microserviceName,
                                          @Validated @RequestBody Release release) {

        createRelease.execute(microserviceName, release);

        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("{microserviceName}/releases")
    public ResponseEntity<ServiceReleaseListResponseDTO> getAll(@PathVariable String microserviceName) {

        List<String> releaseNames = getAllReleasesOfService.execute(microserviceName);

        return ResponseEntity.ok()
                             .body(new ServiceReleaseListResponseDTO(microserviceName, releaseNames));
    }
}
