package com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter;

import org.yaml.snakeyaml.DumperOptions;

public class CustomDumperOptions {
    public static DumperOptions getCustomDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        // Fix below - additional configuration
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        return options;
    }
}
