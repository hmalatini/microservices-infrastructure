package com.infra.resources.adapter.controller.secret;

import com.infra.resources.core.domain.Secret;
import com.infra.resources.core.usecase.secret.CreateSecret;
import com.infra.resources.core.usecase.secret.DeleteSecret;
import com.infra.resources.core.usecase.secret.GetAllSecretsOfService;
import com.infra.resources.core.usecase.secret.UpdateSecret;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("backend/{microserviceName}/secrets")
@RequiredArgsConstructor
public class SecretController {

    private final CreateSecret createSecret;
    private final DeleteSecret deleteSecret;
    private final UpdateSecret updateSecret;
    private final GetAllSecretsOfService getAllSecretsOfService;

    @PostMapping
    public ResponseEntity<Secret> create(@Validated @RequestBody SecretCreation secretCreation,
                                         @PathVariable String microserviceName) {

        Secret s = createSecret.execute(secretCreation, microserviceName);

        return ResponseEntity.ok()
                             .body(s);
    }

    @GetMapping
    public ResponseEntity<List<Secret>> getSecrets(@PathVariable String microserviceName) {

        List<Secret> secrets = getAllSecretsOfService.execute(microserviceName);

        return ResponseEntity.ok()
                             .body(secrets);
    }

    @GetMapping("all")
    public ResponseEntity<List<Secret>> getHiddenSecrets(@PathVariable String microserviceName) {

        List<Secret> secrets = getAllSecretsOfService.execute(microserviceName)
                                                     .stream()
                                                     .map(this::hideSensibleValue)
                                                     .collect(Collectors.toList());

        return ResponseEntity.ok()
                             .body(secrets);
    }

    @PutMapping("/{secretName}")
    public ResponseEntity<Secret> update(@PathVariable String microserviceName, @PathVariable String secretName,
                                         @Validated @RequestBody SecretUpdate secretUpdate) {

        Secret s = updateSecret.execute(microserviceName, secretName, secretUpdate);

        return ResponseEntity.ok()
                             .body(s);
    }

    @DeleteMapping("/{secretName}")
    public ResponseEntity<Secret> delete(@PathVariable String microserviceName, @PathVariable String secretName) {

        Secret s = deleteSecret.execute(microserviceName, secretName);

        return ResponseEntity.ok()
                             .body(s);
    }

    private Secret hideSensibleValue(Secret secret) {
        if (secret.isSensitive()) {
            secret.setValue("******");
        }

        return secret;
    }
}
