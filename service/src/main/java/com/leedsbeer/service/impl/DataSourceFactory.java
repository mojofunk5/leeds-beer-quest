package com.leedsbeer.service.impl;

import com.leedsbeer.service.LeedsBeerApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    private DataSourceFactory() {
        super();
    }

    public static DataSource create(LeedsBeerApplicationProperties properties) {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(properties.serviceJdbcUrl());
        configuration.setUsername(properties.dbServiceUsername());
        configuration.setPassword(properties.dbServicePassword());
        return new HikariDataSource(configuration);
    }
}