package com.leedsbeer.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedsbeer.service.domain.Ratings;
import com.leedsbeer.service.domain.Venue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;

import java.time.Instant;

import static com.leedsbeer.service.test.TestData.aRandomVenue;
import static com.leedsbeer.service.test.TestData.aRandomVenueBuilder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class VenueDtoTest {

    public static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.create();

    @Test
    public void canCreateFromDomain() {
        Venue venueDomain = aRandomVenue();
        VenueDto venueDto = VenueDto.from(venueDomain);

        assertThat(venueDto.getId(), is(venueDomain.getId()));
        assertThat(venueDto.getName(), is(venueDomain.getName()));
        assertThat(venueDto.getCategory(), is(venueDomain.getCategory()));
        assertThat(venueDto.getTimestamp(), is(venueDomain.getTimestamp()));
        assertThat(venueDto.getExcerpt(), is(venueDomain.getExcerpt()));
        assertThat(venueDto.getUrl(), is(venueDomain.getUrl()));
        assertThat(venueDto.getThumbnail(), is(venueDomain.getThumbnail()));
        assertThat(venueDto.getLatitude(), is(venueDomain.getLatitude()));
        assertThat(venueDto.getLongitude(), is(venueDomain.getLongitude()));

        Ratings ratingsDomain = venueDomain.getRatings();
        RatingsDto ratingsDto = venueDto.getRatings();

        assertThat(ratingsDto.getBeer(), is(ratingsDomain.getBeer()));
        assertThat(ratingsDto.getAtmosphere(), is(ratingsDomain.getAtmosphere()));
        assertThat(ratingsDto.getAmenities(), is(ratingsDomain.getAmenities()));
        assertThat(ratingsDto.getValue(), is(ratingsDomain.getValue()));

        assertThat(venueDto.getAddress(), is(venueDomain.getAddress()));
        assertThat(venueDto.getPhone(), is(venueDomain.getPhone()));
        assertThat(venueDto.getTwitterHandle(), is(venueDomain.getTwitterHandle()));
        assertThat(venueDto.getTags(), hasItems(venueDomain.getTags().toArray(new String[]{})));
    }

    @Test
    public void hasToStringMethodImplemented() {
        VenueDto dto = VenueDto.from(aRandomVenue());
        String toString = dto.toString();
        assertThat(toString, containsString("VenueDto["));
        assertThat(toString, containsString("RatingsDto["));
    }

    @Test
    public void canRoundtripAsJson() {
        VenueDto originalDto = VenueDto.from(aRandomVenue());
        VenueDto roundtrippedDto = roundtripAsJson(originalDto);

        boolean simpleFieldsEqual = EqualsBuilder.reflectionEquals(originalDto, roundtrippedDto, "ratings", "tags");
        assertThat(simpleFieldsEqual, is(true));
        assertThat(EqualsBuilder.reflectionEquals(originalDto.getRatings(), roundtrippedDto.getRatings()), is(true));
        assertThat(originalDto.getTags(), hasItems(roundtrippedDto.getTags().toArray(new String[]{})));
    }

    @Test
    public void timestampSerialisedAsHumanReadableString() {
        VenueDto dto = VenueDto.from(aRandomVenueBuilder().timestamp(Instant.parse("2022-03-05T12:23:17.043Z")).build());
        String json = writeAsJson(dto);

        assertThat(json, containsString("2022-03-05T12:23:17.043Z"));
    }

    private VenueDto roundtripAsJson(VenueDto dto) {
        try {
            String json = writeAsJson(dto);
            return OBJECT_MAPPER.readValue(json, VenueDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String writeAsJson(VenueDto dto) {
        try {
            return OBJECT_MAPPER.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}