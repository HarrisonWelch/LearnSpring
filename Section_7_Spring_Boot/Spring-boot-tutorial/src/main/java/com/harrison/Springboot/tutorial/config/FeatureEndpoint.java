package com.harrison.Springboot.tutorial.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "features") // Part of SB Actuate
public class FeatureEndpoint {

    private final Map<String, Feature> featuresMap = new ConcurrentHashMap<>();

    public FeatureEndpoint() {
        featuresMap.put("Department", new Feature(true));
        featuresMap.put("User", new Feature(false)); // false b/c not impl
        featuresMap.put("Authentication", new Feature(false)); // false b/c not impl
    }

    // Send feat back when called
    @ReadOperation // write and delete op also exist
    public Map<String, Feature> features() {
        return featuresMap;
    }

    @ReadOperation
    public Feature feature(@Selector String featureName) {
        return featuresMap.get(featureName);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Feature {
        private boolean isEnabled;
    }
}
