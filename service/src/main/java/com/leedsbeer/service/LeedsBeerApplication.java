package com.leedsbeer.service;

import com.leedsbeer.service.domain.VenueRepository;
import com.leedsbeer.service.http.JavalinFactory;
import com.leedsbeer.service.http.VenueEndpoint;
import com.leedsbeer.service.impl.DataSourceFactory;
import com.leedsbeer.service.impl.JdbcVenueRepository;
import io.javalin.Javalin;

import javax.sql.DataSource;

import static com.leedsbeer.service.LeedsBeerApplicationProperties.someProperties;

public class LeedsBeerApplication {

    private final Javalin javalin;

    public static void main(String[] args) {
        start(someProperties().build());
    }

    private LeedsBeerApplication(LeedsBeerApplicationProperties properties) {
        upgradeDatabase(properties);

        DataSource dataSource = DataSourceFactory.create(properties);
        VenueRepository venueRepository = new JdbcVenueRepository(dataSource);

        javalin = JavalinFactory.create();
        VenueEndpoint.createAndRegister(javalin, venueRepository);
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