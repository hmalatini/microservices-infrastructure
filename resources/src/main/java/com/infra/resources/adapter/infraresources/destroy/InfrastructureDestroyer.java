package com.infra.resources.adapter.infraresources.destroy;

import com.infra.resources.adapter.gateway.terraformcloud.TerraformCloudGateway;
import com.infra.resources.core.exceptions.DestroyResourceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
class InfrastructureDestroyer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfrastructureDestroyer.class);

    private static final int RESOURCE_CHECK_TIME_OUT = 300;
    private static final int RESOURCE_CHECK_INTERVAL_TIME = 5;

    private final TerraformCloudGateway terraformCloudGateway;

    void destroyInfrastructure(String serviceName, String resourceName, String terraformWorkspaceId) {
        String runId = terraformCloudGateway.run(terraformWorkspaceId, "deleting resource", true);

        Instant startInstant = Instant.now();
        Instant checkRunInstant = Instant.now();
        String status = "pending";

        while (!status.equals("applied") && !status.equals("planned_and_finished")) {
            Instant current = Instant.now();

            isError(serviceName, resourceName, terraformWorkspaceId, runId, status);
            isTimeOut(serviceName, resourceName, terraformWorkspaceId, runId, startInstant, current);

            if (shouldCheckStatus(checkRunInstant, current)) {
                status = getTerraformStatus(serviceName, resourceName, terraformWorkspaceId, runId);
                checkRunInstant = current;
            }
        }
    }

    private boolean shouldCheckStatus(Instant checkRunInstant, Instant current) {
        return Duration.between(checkRunInstant, current).getSeconds() > RESOURCE_CHECK_INTERVAL_TIME;
    }

    private String getTerraformStatus(String serviceName, String resourceName, String terraformWorkspaceId, String runId) {
        String status = terraformCloudGateway.getRunStatus(runId);
        LOGGER.debug("[service:{}] [resource:{}] [workspaceId:{}] [runId:{}] Destroying resource. Status of terraform is {}", serviceName, resourceName, terraformWorkspaceId, runId, status);
        return status;
    }

    private void isTimeOut(String serviceName, String resourceName, String terraformWorkspaceId, String runId, Instant startInstant, Instant current) {
        if (Duration.between(startInstant, current).getSeconds() > RESOURCE_CHECK_TIME_OUT) {
            LOGGER.error("[service:{}] [resource:{}] [workspaceId:{}] [runId:{}] Error destroy resource. Time out reached", serviceName, resourceName, terraformWorkspaceId, runId);
            throw new DestroyResourceException(String.format("Time out waiting for run %s to destroy resource %s on service %s", runId, resourceName, serviceName));
        }
    }

    private void isError(String serviceName, String resourceName, String terraformWorkspaceId, String runId, String status) {
        if (status.equals("errored")) {
            LOGGER.error("[service:{}] [resource:{}] [workspaceId:{}] [runId:{}] Error destroy resource. Terraform plan/apply fails", serviceName, resourceName, terraformWorkspaceId, runId);
            throw new DestroyResourceException(String.format("Error destroy resource for run %s to destroy resource %s on service %s. Terraform plan/apply fails", runId, resourceName, serviceName));
        }
    }
}
