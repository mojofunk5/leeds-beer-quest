package com.leedsbeer.service;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    private final Flyway flyway;

    public DatabaseMigration(LeedsBeerApplicationProperties properties) {
        this.flyway = Flyway.configure()
                .createSchemas(true)
                .schemas("BEER")
                .dataSource(properties.flywayJdbcUrl(), properties.dbFlywayUsername(), properties.dbFlywayPassword())
                .locations(properties.flywayMigrationLocations())
                .load();
    }

    public void migrate() {
        flyway.migrate();
    }
}
