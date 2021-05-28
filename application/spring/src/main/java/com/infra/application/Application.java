package com.infra.application;

import com.infra.application.config.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.infra"})
@EnableJpaRepositories(basePackages = {"com.infra"})
@EntityScan(basePackages = {"com.infra"})
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        String profile = getActiveProfile();
        String serviceName = System.getenv("SERVICE_NAME");
        LOGGER.info("Starting {} with env {}.", serviceName, profile);

        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles(profile);

        SpringApplication app = new SpringApplication(Application.class);
        app.setEnvironment(environment);
        app.run(args);
    }

    private static String getActiveProfile() {
        String environment = System.getenv("ENVIRONMENT").toLowerCase();
        return Profiles.getProfiles().contains(environment) ? environment : Profiles.DEV;
    }
}
