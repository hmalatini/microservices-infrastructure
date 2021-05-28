package com.infra.resources.adapter.infraresources.creation;

import com.infra.resources.core.enums.ResourceType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
class ResourceCreatorFactory {

    private final Map<ResourceType, ResourceCreator> resourceMap;

    public ResourceCreatorFactory(MicroserviceResourceCreator microserviceResource, EnvironmentResourceCreator environmentResource) {
        this.resourceMap = new EnumMap<>(ResourceType.class);
        resourceMap.put(ResourceType.MICROSERVICE, microserviceResource);
        resourceMap.put(ResourceType.ENVIRONMENT, environmentResource);
    }

    ResourceCreator getResource(ResourceType type) {
        return resourceMap.getOrDefault(type, null);
    }
}
