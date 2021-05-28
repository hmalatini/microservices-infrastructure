package com.infra.deployment.adapter.deploymentyamlgenerator;

import com.infra.deployment.adapter.deploymentyamlgenerator.dto.Autoscaling;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.HelmChart;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.HelmValue;
import com.infra.deployment.adapter.deploymentyamlgenerator.dto.Image;
import com.infra.deployment.adapter.deploymentyamlgenerator.resourcemanager.DeploymentTemplateManager;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.CustomDumperOptions;
import com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter.EmptyRepresenter;
import com.infra.deployment.adapter.gateway.cronjob.CronJobGateway;
import com.infra.deployment.adapter.gateway.environment.EnvironmentGateway;
import com.infra.deployment.adapter.gateway.networking.NetworkingGateway;
import com.infra.deployment.adapter.gateway.secrets.SecretsGateway;
import com.infra.deployment.config.RegistryDeploymentConfig;
import com.infra.deployment.core.domain.Deployment;
import com.infra.deployment.core.exceptions.HelmDeploymentCreatorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
@RequiredArgsConstructor
public class HelmDeploymentCreator {

    private final Yaml yaml = new Yaml(new EmptyRepresenter(), CustomDumperOptions.getCustomDumperOptions());
    private final DeploymentTemplateManager deploymentTemplateManager;
    private final EnvironmentGateway environmentGateway;
    private final SecretsGateway secretsGateway;
    private final CronJobGateway cronJobGateway;
    private final NetworkingGateway networkingGateway;
    private final RegistryDeploymentConfig registryConfig;

    public void createDeployment(Deployment deployment, File dir) {
        File envDir = deploymentTemplateManager.createDirAndDownloadTemplateFiles(dir.getPath(),
            deployment.getEnvironmentName());
        createDeploymentFiles(deployment, envDir);
    }

    private void createDeploymentFiles(Deployment deployment, File dir) {
        HelmChart chart = getHelmChart(deployment);
        createChartYamlFile(chart, dir);

        HelmValue values = getHelmValues(deployment);
        createValuesYamlFile(values, dir);
    }

    private void createValuesYamlFile(HelmValue values, File dir) {
        try (FileWriter writer = new FileWriter(dir.getPath() + "/values.yaml")) {
            String yamlString = yaml.dump(values);
            writer.write(processYaml(yamlString));
            writer.flush();
        } catch (IOException e) {
            throw new HelmDeploymentCreatorException("error creating values.yaml file", e);
        }
    }

    private void createChartYamlFile(HelmChart chart, File dir) {
        try (FileWriter writer = new FileWriter(dir.getPath() + "/Chart.yaml")) {
            String yamlString = yaml.dump(chart);
            writer.write(processYaml(yamlString));
            writer.flush();
        } catch (IOException e) {
            throw new HelmDeploymentCreatorException("error creating Chart.yaml file", e);
        }
    }

    private HelmValue getHelmValues(Deployment deployment) {
        Autoscaling autoscaling = getAutoscalingValues(deployment);
        return HelmValue.getDefaultValuesBuilder()
                        .namespace(deployment.getMicroserviceName() + "-" + deployment.getEnvironmentName())
                        .environment(deployment.getEnvironmentName())
                        .nameOverride(deployment.getMicroserviceName())
                        .fullnameOverride(deployment.getVersion())
                        .image(Image.builder()
                                    .repository(
                                        registryConfig.getRegistryUser() + "/" + registryConfig.getRegistryRepo())
                                    .tag(deployment.getVersion())
                                    .pullPolicy("IfNotPresent")
                                    .build())
                        .configValues(secretsGateway.getSecretsOfService(deployment.getMicroserviceName()))
                        .cronjobs(cronJobGateway.getCronJobsOfServiceAndEnvironment(deployment.getMicroserviceName(),
                            deployment.getEnvironmentName()))
                        .autoscaling(autoscaling)
                        .replicaCount(autoscaling.getMinReplicas())
                        .networking(networkingGateway.getNetworkingConfig(deployment.getMicroserviceName(),
                            deployment.getEnvironmentName()))
                        .build();
    }

    private Autoscaling getAutoscalingValues(Deployment deployment) {
        Autoscaling autoscaling;
        if (deployment.isUseDefaultAutoscalingValues()) {
            autoscaling = Autoscaling.builder()
                                     .enabled(true)
                                     .minReplicas(1)
                                     .maxReplicas(5)
                                     .targetCPUUtilizationPercentage(80)
                                     .build();
        } else {
            autoscaling = environmentGateway.getAutoscalingValues(deployment.getMicroserviceName(),
                deployment.getEnvironmentName());
        }
        return autoscaling;
    }


    private HelmChart getHelmChart(Deployment deployment) {
        return HelmChart.getDefaultValuesBuilder()
                        .name(deployment.getMicroserviceName())
                        .appVersion(deployment.getVersion())
                        .build();
    }

    private String processYaml(String yaml) {
        String processedYaml = yaml;
        processedYaml = processedYaml.replace("!empty 'emptyarray'", "[]");
        processedYaml = processedYaml.replace("!empty 'emptyobject'", "{}");
        processedYaml = processedYaml.replace("!empty 'emptystring'", "\"\"");
        processedYaml = processedYaml.replaceAll(
            "!!com\\.infra\\.deployment\\.adapter\\.deploymentyamlgenerator\\.dto\\.HelmValue\n", "");
        processedYaml = processedYaml.replaceAll(
            "!!com\\.infra\\.deployment\\.adapter\\.deploymentyamlgenerator\\.dto\\.HelmChart\n", "");
        processedYaml = processedYaml.replaceAll(
            "!!com\\.infra\\.deployment\\.adapter\\.deploymentyamlgenerator\\.dto\\.networking\\.HeaderConfigValue\n {12}",
            "");
        return processedYaml;
    }
}
