package com.leedsbeer.service.impl;

import com.leedsbeer.service.DatabaseMigration;
import com.leedsbeer.service.LeedsBeerApplicationProperties;
import com.leedsbeer.service.domain.Ratings;
import com.leedsbeer.service.domain.Venue;
import com.leedsbeer.service.domain.VenueRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Optional;

import static com.leedsbeer.service.LeedsBeerApplicationProperties.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JdbcVenueRepositoryTest {

    private static LeedsBeerApplicationProperties properties;

    private VenueRepository repository;

    @BeforeClass
    public static void beforeClass() {
        properties = someProperties()
                .with(PROPERTY_DB_FLYWAY_MIGRATION_LOCATIONS, "classpath:db/migration/common,classpath:db/migration/h2")
                .with(PROPERTY_DB_FLYWAY_JDBC_URL, "jdbc:h2:mem:BEER;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:db/migration/test/init_tests.sql';")
                .with(PROPERTY_DB_FLYWAY_USERNAME, "root")
                .with(PROPERTY_DB_FLYWAY_PASSWORD, "password")
                .with(PROPERTY_DB_SERVICE_JDBC_URL, "jdbc:h2:mem:BEER;")
                .with(PROPERTY_DB_SERVICE_USERNAME, "BEER_SERVICE")
                .with(PROPERTY_DB_SERVICE_PASSWORD, "abc")
                .build();

        DatabaseMigration databaseMigration = new DatabaseMigration(properties);
        databaseMigration.migrate();
    }

    @Before
    public void before() {
        DataSource dataSource = DataSourceFactory.create(properties);
        this.repository = new JdbcVenueRepository(dataSource);
    }

    @Test
    public void doesNotFindVenueForNonExistingId() {
        assertThat(repository.findById(1_000_000), is(Optional.empty()));
    }

    @Test
    public void doesFindVenueForExistingId() {
        assertThat(repository.findById(1), is(not(Optional.empty())));
    }

    @Test
    public void venueHasValuesSetCorrectly() {
        Venue venue = repository.findById(1).get();
        assertThat(venue.getId(), is(1));
        assertThat(venue.getName(), is("...escobar"));
        assertThat(venue.getCategory(), is("Closed venues"));
        assertThat(venue.getUrl(), is("http://leedsbeer.info/?p=765"));
        assertThat(venue.getExcerpt(), is("...It's really dark in here!"));
        assertThat(venue.getThumbnail(), is("http://leedsbeer.info/wp-content/uploads/2012/11/20121129_185815.jpg"));
        assertThat(venue.getTimestamp(), is(Instant.parse("2012-11-30T21:58:52Z")));
        assertThat(venue.getLatitude(), is(53.8007317));
        assertThat(venue.getLongitude(), is(-1.5481764));
        assertThat(venue.getTwitterHandle(), is("EscobarLeeds"));
        assertThat(venue.getAddress(), is("23-25 Great George Street, Leeds LS1 3BB"));
        assertThat(venue.getPhone(), is("0113 220 4389"));
        assertThat(venue.getTags(), hasItems("food", "live music", "sofas"));

        Ratings ratings = venue.getRatings();
        assertThat(ratings.getBeer(), is(2.0));
        assertThat(ratings.getAmenities(), is(3.0));
        assertThat(ratings.getAtmosphere(), is(3.0));
        assertThat(ratings.getValue(), is(3.0));
    }
}