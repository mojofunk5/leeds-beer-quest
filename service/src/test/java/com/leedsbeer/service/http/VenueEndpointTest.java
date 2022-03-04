package com.leedsbeer.service.http;

import com.leedsbeer.service.domain.Venue;
import com.leedsbeer.service.domain.VenueRepository;
import com.leedsbeer.service.impl.JdbcException;
import io.javalin.Javalin;
import io.javalin.http.HttpCode;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.RandomUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import static com.leedsbeer.service.test.TestData.aRandomVenue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VenueEndpointTest {

    public static int RANDOM_PORT = 0;

    private static OkHttpClient httpClient;

    private Javalin javalin;

    @Mock
    private VenueRepository mockVenueRepository;

    @BeforeClass
    public static void beforeClass() {
        httpClient = new OkHttpClient.Builder().build();
    }

    @AfterClass
    public static void afterClass() {
        httpClient.dispatcher().executorService().shutdown();
    }

    @Before
    public void before() {
        javalin = JavalinFactory.create();
        VenueEndpoint.createAndRegister(javalin, mockVenueRepository);
        javalin.start(RANDOM_PORT);
    }

    @After
    public void after() {
        javalin.stop();
    }

    @Test
    public void getByIdReturnsNotFoundCodeWhenVenueDoesNotExist() {
        when(mockVenueRepository.findById(anyInt())).thenReturn(Optional.empty());

        Response response = getVenueById(RandomUtils.nextInt(0, 100_000));
        assertThat(response.code(), is(HttpCode.NOT_FOUND.getStatus()));
    }

    @Test
    public void getByIdReturnsOkCodeWhenVenueDoesExist() {
        Venue venue = aRandomVenue();

        when(mockVenueRepository.findById(venue.getId())).thenReturn(Optional.of(venue));

        Response response = getVenueById(venue.getId());
        assertThat(response.code(), is(HttpCode.OK.getStatus()));
    }

    @Test
    public void getByIdReturnsInternalErrorCodeIfRepositoryThrowsException() {
        when(mockVenueRepository.findById(anyInt())).thenThrow(new JdbcException("Something went wrong",
                new IllegalStateException()));

        Response response = getVenueById(RandomUtils.nextInt(0, 100_000));
        assertThat(response.code(), is(HttpCode.INTERNAL_SERVER_ERROR.getStatus()));
    }

    @Test
    public void getByIdReturnsBadRequestIfIdNotNumeric() {
        Response response = getVenueById("notanumber");
        assertThat(response.code(), is(HttpCode.BAD_REQUEST.getStatus()));
    }

    private Response getVenueById(Object id) {
        Call call = httpClient.newCall(httpGetRequest("venue/" + id));
        try {
            return call.execute();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Request httpGetRequest(String path) {
        String url = String.format("http://localhost:%s/%s", javalin.port(), path);
        return new Request.Builder().url(url).get().build();
    }
}