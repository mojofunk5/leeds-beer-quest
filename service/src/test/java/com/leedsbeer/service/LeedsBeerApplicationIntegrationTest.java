package com.leedsbeer.service;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;

import static com.leedsbeer.service.LeedsBeerApplicationProperties.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LeedsBeerApplicationIntegrationTest {

    public static final String RANDOM_PORT = "0";

    private static LeedsBeerApplication application;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(PROPERTY_SERVICE_PORT, RANDOM_PORT);
        System.setProperty(PROPERTY_DB_FLYWAY_MIGRATION_LOCATIONS, "classpath:db/migration/common,classpath:db/migration/h2");
        System.setProperty(PROPERTY_DB_JDBC_URL, "jdbc:h2:mem:BEER;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/migration/test/init_tests.sql';");
        System.setProperty(PROPERTY_DB_FLYWAY_USERNAME, "root");
        System.setProperty(PROPERTY_DB_FLYWAY_PASSWORD, "password");
        application = LeedsBeerApplication.start();
    }

    @AfterClass
    public static void afterClass() {
        application.stop();
    }

    @Test
    public void canGetOkResponseFromHelloWorldEndpoint() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Call call = httpClient.newCall(httpGetRequest("hello"));
        Response response = call.execute();
        assertThat(response.code(), is(200));
    }

    @Test
    public void flywayScriptsAreRunSuccessfully() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:BEER", "BEER_SERVICE", "abc")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM BEER.VENUE")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    assertThat(resultSet.getInt(1), is(242));
                }
            }
        }
    }

    private Request httpGetRequest(String path) {
        String url = String.format("http://localhost:%s/%s", application.port(), path);
        return new Request.Builder().url(url).get().build();
    }
}