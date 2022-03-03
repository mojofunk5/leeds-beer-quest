package com.leedsbeer.service;

import io.javalin.Javalin;

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
        DatabaseMigration databaseMigration = new DatabaseMigration(properties);
        databaseMigration.migrate();
    }
}