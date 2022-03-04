package com.leedsbeer.service.test;

import com.leedsbeer.service.domain.Ratings;
import com.leedsbeer.service.domain.Venue;
import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;

import static com.leedsbeer.service.domain.Ratings.someRatings;
import static com.leedsbeer.service.domain.Venue.aVenue;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class TestData {

    private TestData() {
        super();
    }

    public static Venue aRandomVenue() {
        return aVenue()
                .id(RandomUtils.nextInt(0, 100_000))
                .name(randomAlphanumeric(10))
                .category(randomAlphanumeric(5))
                .url(randomAlphanumeric(20))
                .timestamp(Instant.now())
                .excerpt(randomAlphanumeric(15))
                .thumbnail(randomAlphanumeric(20))
                .latitude(RandomUtils.nextDouble(0, 60))
                .longitude(RandomUtils.nextDouble(0, 60))
                .address(randomAlphanumeric(30))
                .phone(randomNumeric(12))
                .twitterHandle(randomNumeric(12))
                .ratings(someRandomRatings())
                .tags(randomAlphanumeric(5), randomAlphanumeric(6))
                .build();
    }

    public static Ratings someRandomRatings() {
        return someRatings()
                .beer(RandomUtils.nextDouble(0, 5))
                .atmosphere(RandomUtils.nextDouble(0, 5))
                .amenities(RandomUtils.nextDouble(0, 5))
                .value(RandomUtils.nextDouble(0, 5))
                .build();
    }
}