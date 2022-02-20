package com.leedsbeer.service;

public class LeedsBeerApplicationProperties {

    public int servicePort() {
        return getIntProperty("SERVICE_PORT", 8080);
    }

    public String jdbcUrl() {
        return getMandatoryStringProperty("DB_JDBC_URL");
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
