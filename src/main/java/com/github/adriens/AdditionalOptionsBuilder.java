package com.github.adriens;

import schemacrawler.tools.options.Config;
import schemacrawler.tools.text.base.BaseTextOptionsBuilder;

import java.util.HashMap;
import java.util.Map;

import static us.fatehi.utility.Utility.isBlank;

public final class AdditionalOptionsBuilder extends BaseTextOptionsBuilder<AdditionalOptionsBuilder, AdditionalOptions> {

    private static final String CLI_DB_ENV = "dbenv";
    private static final String CLI_DB_ID = "dbid";
    private static final String ADDITIONAL_LINT_PREFIX = "additional.lint.";
    private static final String DB_ENV = ADDITIONAL_LINT_PREFIX + CLI_DB_ENV;
    private static final String DB_ID = ADDITIONAL_LINT_PREFIX + CLI_DB_ID;

    String dbEnv;
    String dbId;
    Map<String, String> properties;

    public static AdditionalOptionsBuilder builder() {
        return new AdditionalOptionsBuilder();
    }

    public static AdditionalOptionsBuilder builder(final AdditionalOptions options) {
        return new AdditionalOptionsBuilder().fromOptions(options);
    }

    @Override
    public AdditionalOptions toOptions() {
        return new AdditionalOptions(this);
    }

    @Override
    public AdditionalOptionsBuilder fromConfig(final Config config) {
        if (config == null) {
            return this;
        }
        super.fromConfig(config);

        final String dbEnvConfigsKey;
        if (config.containsKey(CLI_DB_ENV)) {
            // Honor command-line option first
            dbEnvConfigsKey = config.getStringValue(CLI_DB_ENV, "");
        } else {
            // Otherwise, take option from SchemaCrawler configuration file
            dbEnvConfigsKey = DB_ENV;
        }
        dbEnv = config.getStringValue(dbEnvConfigsKey, "");

        final String dbIdConfigsKey;
        if (config.containsKey(CLI_DB_ID)) {
            // Honor command-line option first
            dbIdConfigsKey = config.getStringValue(CLI_DB_ID, "");
        } else {
            // Otherwise, take option from SchemaCrawler configuration file
            dbIdConfigsKey = DB_ID;
        }
        dbId = config.getStringValue(dbIdConfigsKey, "");
        return this;
    }

    @Override
    public AdditionalOptionsBuilder fromOptions(final AdditionalOptions options) {
        if (options == null) {
            return this;
        }
        super.fromOptions(options);

        dbEnv = options.getDbEnv();
        dbId = options.getDbId();

        return this;
    }

    private AdditionalOptionsBuilder() {

        dbEnv = "";
        dbId = "";
        properties = new HashMap<>();
    }

    @Override
    public Config toConfig() {
        final Config config = super.toConfig();
        config.setStringValue(DB_ENV, dbEnv);
        config.setStringValue(DB_ID, dbId);
        return config;
    }

    /** With the db env. */
    public AdditionalOptionsBuilder withDbEnv(final String dbEnv) {
        if (dbEnv == null) {
            this.dbEnv = "";
        } else {
            this.dbEnv = dbEnv;
        }
        return this;
    }

    /** With the db Id. */
    public AdditionalOptionsBuilder withDbId(final String dbId) {
        if (dbId == null) {
            this.dbId = "";
        } else {
            this.dbId = dbId;
        }
        return this;
    }

    /** With properties. */
    public AdditionalOptionsBuilder withProperties(final Map<String, String> properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
        return this;
    }

    /** With properties. */
    public AdditionalOptionsBuilder withProperty(final String name, final String value) {
        if (!isBlank(name)) {
            this.properties.put(name, value);
        }
        return this;
    }
}
