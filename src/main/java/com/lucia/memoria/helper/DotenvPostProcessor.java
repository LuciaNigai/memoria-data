package com.lucia.memoria.helper;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DotenvPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Get active profiles
        String[] activeProfiles = environment.getActiveProfiles();

        // Check if 'dev' profile is active
        for (String profile : activeProfiles) {
            if ("dev".equalsIgnoreCase(profile)) {
                // Load environment variables from dev.env file
                Dotenv dotenv = Dotenv.configure()
                        .directory(".") // Point to the root directory if dev.env is there
                        .filename("dev.env")  // Specify the dev.env file explicitly
                        .ignoreIfMissing()
                        .load();

                Map<String, Object> props = new HashMap<>();
                dotenv.entries().forEach(entry -> props.put(entry.getKey(), entry.getValue()));

                // Add the properties to the Spring Environment
                environment.getPropertySources().addFirst(new MapPropertySource("dotenv", props));
                break;  // Once 'dev' profile is found, stop checking
            }
        }
    }
}
