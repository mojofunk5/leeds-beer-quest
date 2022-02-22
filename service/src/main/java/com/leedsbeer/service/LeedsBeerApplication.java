package com.leedsbeer.service;

import io.javalin.Javalin;
import org.flywaydb.core.Flyway;

import java.util.Map;

import static com.leedsbeer.service.LeedsBeerApplicationProperties.someProperties;

public class LeedsBeerApplication {

    private final Javalin javalin;

    public static void main(String[] args) {
        start(someProperties().build());
    }

    private LeedsBeerApplication(LeedsBeerApplicationProperties properties) {
        upgradeDatabase(properties);
        javalin = Javalin.create();
        javalin.get("hello", ctx -> ctx.json(Map.of("hello", "world")));
        javalin.start(properties.servicePort());
    }

    static LeedsBeerApplication start(LeedsBeerApplicationProperties properties) {
        return new LeedsBeerApplication(properties);
    }

    public void stop() {
        javalin.stop();
    }

    int port() {
        return javalin.port();
    }

    private void upgradeDatabase(LeedsBeerApplicationProperties properties) {
        Flyway flyway = Flyway.configure()
                .createSchemas(true)
                .schemas("BEER")
                .dataSource(properties.jdbcUrl(), properties.dbFlywayUsername(), properties.dbFlywayPassword())
                .locations(properties.flywayMigrationLocations())
                .load();
        flyway.migrate();
    }
}