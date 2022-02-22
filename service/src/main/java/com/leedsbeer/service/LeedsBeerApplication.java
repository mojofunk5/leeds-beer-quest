package com.leedsbeer.service;

import io.javalin.Javalin;
import org.flywaydb.core.Flyway;

import java.util.Map;

public class LeedsBeerApplication {

    private final Javalin javalin;

    public static void main(String[] args) {
        start();
    }

    private LeedsBeerApplication(LeedsBeerApplicationProperties properties) {
        upgradeDatabase(properties);
        javalin = Javalin.create();
        javalin.get("hello", ctx -> ctx.json(Map.of("hello", "world")));
        javalin.start(properties.servicePort());
    }

    static LeedsBeerApplication start() {
        LeedsBeerApplicationProperties properties = new LeedsBeerApplicationProperties();
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