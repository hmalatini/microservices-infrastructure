package com.infra.application.config;


import java.util.HashSet;
import java.util.Set;

public class Profiles {

    private Profiles() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PROD = "prod";
    public static final String STAGE = "stage";
    public static final String TEST = "test";
    public static final String DEV = "dev";

    private static final Set<String> values;

    static {
        values = new HashSet<>();
        values.add(PROD);
        values.add(STAGE);
        values.add(TEST);
        values.add(DEV);
    }

    public static Set<String> getProfiles() {
        return values;
    }
}
