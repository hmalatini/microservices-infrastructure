package com.infra.resources.adapter.infraresources.creation;

import com.infra.resources.adapter.controller.environment.dto.EnvironmentCreation;
import com.infra.resources.adapter.controller.microservice.dto.MicroserviceCreation;
import com.infra.resources.core.domain.Cluster;
import com.infra.resources.core.domain.Microservice;
import com.infra.resources.core.domain.TerraformVariable;
import com.infra.resources.core.enums.ResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreationResourceManager {

    private final ResourceCreatorFactory resourceCreatorFactory;

    public String createMicroservice(MicroserviceCreation creation) {
        ResourceCreator resourceCreator = resourceCreatorFactory.getResource(ResourceType.MICROSERVICE);
        List<TerraformVariable> terraformVariables = resourceCreator.getPlanVariablesMap(creation.getName(), creation.getTemplate());
        return resourceCreator.createResource(creation.getName() + "-infra", creation.getName(), "/service", creation.getName() + "-service", terraformVariables, "microservice");
    }

    public String createEnvironment(EnvironmentCreation creation, Microservice microservice, Cluster cluster) {
        ResourceCreator resourceCreator = resourceCreatorFactory.getResource(ResourceType.ENVIRONMENT);
        String[] inputValues = {
                microservice.getName() + "-" + creation.getEnvironmentName(),
                microservice.getDeploymentGitUrl() + ".git",
                creation.getEnvironmentName(),
                cluster.getHost(),
                microservice.getName() + "-" + creation.getEnvironmentName()
        };
        List<TerraformVariable> terraformVariables = resourceCreator.getPlanVariablesMap(inputValues);
        return resourceCreator.createResource(microservice.getName() + "-infra", microservice.getName(), "/environment/" + creation.getEnvironmentName(), microservice.getName() + "-env-" + creation.getEnvironmentName(), terraformVariables, "environment");
    }
}
