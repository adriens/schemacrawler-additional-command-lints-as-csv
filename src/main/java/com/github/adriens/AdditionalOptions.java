package com.github.adriens;

import schemacrawler.tools.text.base.BaseTextOptions;

import java.util.HashMap;
import java.util.Map;

public class AdditionalOptions extends BaseTextOptions {
    private final String dbId;
    private final String dbEnv;
    private final Map<String, String> properties;


    public AdditionalOptions(final AdditionalOptionsBuilder builder) {
        super(builder);
        dbEnv = builder.dbEnv;
        dbId = builder.dbId;
        properties = new HashMap<>(builder.properties);
    }

    public String getDbId() {
        return dbId;
    }

    public String getDbEnv() {
        return dbEnv;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "AdditionalOptions{" +
                "dbId='" + dbId + '\'' +
                ", dbEnv='" + dbEnv + '\'' +
                ", properties=" + properties +
                '}';
    }
}
