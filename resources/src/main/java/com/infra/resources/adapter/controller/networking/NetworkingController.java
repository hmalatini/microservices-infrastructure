package com.infra.resources.adapter.controller.networking;

import com.infra.resources.core.domain.networking.Networking;
import com.infra.resources.core.domain.networking.NetworkingEndpoint;
import com.infra.resources.core.domain.networking.NetworkingEndpoint.Type;
import com.infra.resources.core.domain.networking.NetworkingTrafficPolicy;
import com.infra.resources.core.usecase.networking.CreateEndpoint;
import com.infra.resources.core.usecase.networking.CreateTrafficPolicy;
import com.infra.resources.core.usecase.networking.DeleteEndpoint;
import com.infra.resources.core.usecase.networking.DeleteTrafficPolicy;
import com.infra.resources.core.usecase.networking.GetNetworking;
import com.infra.resources.core.usecase.networking.UpdateEndpoint;
import com.infra.resources.core.usecase.networking.UpdateTrafficPolicy;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/{microserviceName}")
@RequiredArgsConstructor
public class NetworkingController {

    private final CreateTrafficPolicy createTrafficPolicy;
    private final UpdateTrafficPolicy updateTrafficPolicy;
    private final DeleteTrafficPolicy deleteTrafficPolicy;
    private final CreateEndpoint createEndpoint;
    private final UpdateEndpoint updateEndpoint;
    private final DeleteEndpoint deleteEndpoint;
    private final GetNetworking getNetworking;

    @PostMapping("{environmentName}/networking/traffic-policy/{name}")
    public ResponseEntity<NetworkingTrafficPolicyDTO> createTrafficPolicy(
        @Validated @RequestBody NetworkingTrafficPolicyDTO configCreation,
        @PathVariable String microserviceName, @PathVariable String environmentName, @PathVariable String name) {

        NetworkingTrafficPolicy config = createTrafficPolicy.execute(microserviceName, environmentName, name,
            configCreation);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapTrafficPolicyToDto(config));
    }

    @PutMapping("{environmentName}/networking/traffic-policy/{name}")
    public ResponseEntity<NetworkingTrafficPolicyDTO> updateTrafficPolicy(
        @Validated @RequestBody NetworkingTrafficPolicyDTO trafficPolicyDTO,
        @PathVariable String microserviceName, @PathVariable String environmentName, @PathVariable String name) {

        NetworkingTrafficPolicy config = updateTrafficPolicy.execute(microserviceName, environmentName, name,
            trafficPolicyDTO);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapTrafficPolicyToDto(config));
    }

    @DeleteMapping("{environmentName}/networking/traffic-policy/{name}")
    public ResponseEntity<NetworkingTrafficPolicyDTO> deleteTrafficPolicy(@PathVariable String microserviceName,
                                                                          @PathVariable String environmentName,
                                                                          @PathVariable String name) {

        NetworkingTrafficPolicy config = deleteTrafficPolicy.execute(microserviceName, environmentName, name);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapTrafficPolicyToDto(config));
    }

    @PostMapping("{environmentName}/networking/endpoint/{name}/private")
    public ResponseEntity<EndpointDTO> createPrivateEndpoint(@Validated @RequestBody EndpointDTO endpointCreation,
                                                             @PathVariable String microserviceName,
                                                             @PathVariable String environmentName,
                                                             @PathVariable String name,
                                                             @RequestParam(required = false) String trafficPolicyName) {

        NetworkingEndpoint endpoint = createEndpoint.execute(microserviceName, environmentName, name, Type.PRIVATE,
            trafficPolicyName, endpointCreation);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @PostMapping("{environmentName}/networking/endpoint/{name}/public")
    public ResponseEntity<EndpointDTO> createPublicEndpoint(@Validated @RequestBody EndpointDTO endpointCreation,
                                                            @PathVariable String microserviceName,
                                                            @PathVariable String environmentName,
                                                            @PathVariable String name,
                                                            @RequestParam(required = false) String trafficPolicyName) {

        NetworkingEndpoint endpoint = createEndpoint.execute(microserviceName, environmentName, name, Type.PUBLIC,
            trafficPolicyName, endpointCreation);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @PutMapping("{environmentName}/networking/endpoint/{name}/private")
    public ResponseEntity<EndpointDTO> updatePrivateEndpoint(@Validated @RequestBody EndpointDTO endpointDTO,
                                                             @PathVariable String microserviceName,
                                                             @PathVariable String environmentName,
                                                             @PathVariable String name,
                                                             @RequestParam(required = false) String trafficPolicyName) {

        NetworkingEndpoint endpoint = updateEndpoint.execute(microserviceName, environmentName, name, Type.PRIVATE,
            trafficPolicyName, endpointDTO);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @PutMapping("{environmentName}/networking/endpoint/{name}/public")
    public ResponseEntity<EndpointDTO> updatePublicEndpoint(@Validated @RequestBody EndpointDTO endpointDTO,
                                                            @PathVariable String microserviceName,
                                                            @PathVariable String environmentName,
                                                            @PathVariable String name,
                                                            @RequestParam(required = false) String trafficPolicyName) {

        NetworkingEndpoint endpoint = updateEndpoint.execute(microserviceName, environmentName, name, Type.PUBLIC,
            trafficPolicyName, endpointDTO);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @DeleteMapping("{environmentName}/networking/endpoint/{name}/private")
    public ResponseEntity<EndpointDTO> deletePrivateEndpoint(@PathVariable String microserviceName,
                                                             @PathVariable String environmentName,
                                                             @PathVariable String name) {

        NetworkingEndpoint endpoint = deleteEndpoint.execute(microserviceName, environmentName, name, Type.PRIVATE);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @DeleteMapping("{environmentName}/networking/endpoint/{name}/public")
    public ResponseEntity<EndpointDTO> deletePublicEndpoint(@PathVariable String microserviceName,
                                                            @PathVariable String environmentName,
                                                            @PathVariable String name) {

        NetworkingEndpoint endpoint = deleteEndpoint.execute(microserviceName, environmentName, name, Type.PUBLIC);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapEndpointToDto(endpoint));
    }

    @GetMapping("{environmentName}/networking")
    public ResponseEntity<NetworkingDTO> getNetworkingByEnvironment(@PathVariable String microserviceName,
                                                       @PathVariable String environmentName) {

        Networking networking = getNetworking.execute(microserviceName, environmentName);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapNetworkingToDto(networking));
    }

    @GetMapping("networking")
    public ResponseEntity<NetworkingDTO> getNetworking(@PathVariable String microserviceName) {

        Networking networking = getNetworking.execute(microserviceName);

        return ResponseEntity.ok()
                             .body(NetworkingControllerMapper.mapNetworkingToDto(networking));
    }
}
