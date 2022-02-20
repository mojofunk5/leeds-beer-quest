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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LeedsBeerApplicationIntegrationTest {

    private static LeedsBeerApplication application;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("SERVICE_PORT", "0");
        System.setProperty("DB_JDBC_URL", "jdbc:h2:mem:BEER;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/migration/test/init_tests.sql';");
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
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:BEER", "root", "password");
        connection.prepareStatement("USE BEER;").execute();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM VENUE");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        assertThat(resultSet.getInt(1), is(242));
    }

    private Request httpGetRequest(String path) {
        String url = String.format("http://localhost:%s/%s", application.port(), path);
        return new Request.Builder().url(url).get().build();
    }
}