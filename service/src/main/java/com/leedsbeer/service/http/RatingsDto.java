package com.leedsbeer.service.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leedsbeer.service.domain.Ratings;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RatingsDto {

    private final double beer;
    private final double atmosphere;
    private final double amenities;
    private final double value;

    public RatingsDto(@JsonProperty("beer") double beer, @JsonProperty("atmosphere") double atmosphere,
                      @JsonProperty("amenities") double amenities, @JsonProperty("value") double value) {
        this.beer = beer;
        this.atmosphere = atmosphere;
        this.amenities = amenities;
        this.value = value;
    }

    public static RatingsDto from(Ratings domain) {
        return new RatingsDto(domain.getBeer(), domain.getAtmosphere(), domain.getAmenities(), domain.getValue());
    }

    public double getBeer() {
        return beer;
    }

    public double getAtmosphere() {
        return atmosphere;
    }

    public double getAmenities() {
        return amenities;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("beer", beer)
                .append("atmosphere", atmosphere)
                .append("amenities", amenities)
                .append("value", value)
                .toString();
    }
}