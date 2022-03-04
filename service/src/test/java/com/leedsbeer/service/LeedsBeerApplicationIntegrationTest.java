package com.leedsbeer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedsbeer.service.http.ObjectMapperFactory;
import com.leedsbeer.service.http.VenueDto;
import com.leedsbeer.service.impl.DataSourceFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.RandomUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.leedsbeer.service.LeedsBeerApplicationProperties.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LeedsBeerApplicationIntegrationTest {

    public static final String RANDOM_PORT = "0";

    private static ObjectMapper objectMapper;
    private static OkHttpClient httpClient;

    private static LeedsBeerApplicationProperties properties;
    private static LeedsBeerApplication application;

    @BeforeClass
    public static void beforeClass() {
        objectMapper = ObjectMapperFactory.create();
        httpClient = new OkHttpClient.Builder().build();

        properties = someProperties()
                .with(PROPERTY_SERVICE_PORT, RANDOM_PORT)
                .with(PROPERTY_DB_FLYWAY_MIGRATION_LOCATIONS, "classpath:db/migration/common,classpath:db/migration/h2")
                .with(PROPERTY_DB_FLYWAY_JDBC_URL, "jdbc:h2:mem:BEER;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/migration/test/init_tests.sql';")
                .with(PROPERTY_DB_FLYWAY_USERNAME, "root")
                .with(PROPERTY_DB_FLYWAY_PASSWORD, "password")
                .with(PROPERTY_DB_SERVICE_JDBC_URL, "jdbc:h2:mem:BEER;")
                .with(PROPERTY_DB_SERVICE_USERNAME, "BEER_SERVICE")
                .with(PROPERTY_DB_SERVICE_PASSWORD, "abc")
                .build();

        application = LeedsBeerApplication.start(properties);
    }

    @AfterClass
    public static void afterClass() {
        application.stop();
        httpClient.dispatcher().executorService().shutdown();
    }

    @Test
    public void canRetrieveVenueById() throws IOException {
        int id = RandomUtils.nextInt(1, 242);

        Call call = httpClient.newCall(httpGetRequest("venue/" + id));
        Response response = call.execute();

        assertThat(response.code(), is(200));

        VenueDto venue = objectMapper.readValue(response.body().string(), VenueDto.class);
        assertThat(venue.getId(), is(id));
    }

    @Test
    public void flywayScriptsAreRunSuccessfully() throws SQLException {
        DataSource dataSource = DataSourceFactory.create(properties);
        try (Connection connection = dataSource.getConnection()) {
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