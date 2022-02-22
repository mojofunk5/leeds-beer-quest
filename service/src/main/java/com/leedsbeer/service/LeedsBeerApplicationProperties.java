package com.leedsbeer.service;

public class LeedsBeerApplicationProperties {

    public static final String PROPERTY_SERVICE_PORT = "SERVICE_PORT";
    public static final String PROPERTY_DB_FLYWAY_MIGRATION_LOCATIONS = "DB_FLYWAY_MIGRATION_LOCATIONS";
    public static final String PROPERTY_DB_JDBC_URL = "DB_JDBC_URL";
    public static final String PROPERTY_DB_FLYWAY_USERNAME = "DB_FLYWAY_USERNAME";
    public static final String PROPERTY_DB_FLYWAY_PASSWORD = "DB_FLYWAY_PASSWORD";

    public int servicePort() {
        return getIntProperty(PROPERTY_SERVICE_PORT, 8080);
    }

    public String[] flywayMigrationLocations() {
        String value = getStringProperty(PROPERTY_DB_FLYWAY_MIGRATION_LOCATIONS,
                "classpath:db/migration/common,classpath:db/migration/mariadb");
        return value.split(",");
    }

    public String jdbcUrl() {
        return getMandatoryStringProperty(PROPERTY_DB_JDBC_URL);
    }

    public String dbFlywayUsername() {
        return getMandatoryStringProperty(PROPERTY_DB_FLYWAY_USERNAME);
    }

    public String dbFlywayPassword() {
        return getMandatoryStringProperty(PROPERTY_DB_FLYWAY_PASSWORD);
    }

    private int getIntProperty(String key, int defaultValue) {
        return Integer.parseInt(getStringProperty(key, defaultValue));
    }

    private String getMandatoryStringProperty(String key) {
        String property = getStringProperty(key, null);
        if (property == null) {
            throw new IllegalArgumentException("Mandatory property=[" + key + "] not set");
        }
        return property;
    }

    private String getStringProperty(String key, Object defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            value = System.getProperty(key);
            if (value == null) {
                return defaultValue == null ? null : defaultValue.toString();
            }
        }
        return value;
    }
}