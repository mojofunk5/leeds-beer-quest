package com.leedsbeer.service.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Ratings {

    private final double beer;
    private final double atmosphere;
    private final double amenities;
    private final double value;

    private Ratings(Builder builder) {
        this.beer = builder.beer;
        this.atmosphere = builder.atmosphere;
        this.amenities = builder.amenities;
        this.value = builder.value;
    }

    public static BeerSetter someRatings() {
        return new Builder();
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("beer", beer)
                .append("atmosphere", atmosphere)
                .append("amenities", amenities)
                .append("value", value)
                .toString();
    }

    public interface BeerSetter {
        AtmosphereSetter beer(double beer);
    }

    public interface AtmosphereSetter {
        AmenitiesSetter atmosphere(double atmosphere);
    }

    public interface AmenitiesSetter {
        ValueSetter amenities(double amenities);
    }

    public interface ValueSetter {
        Builder value(double value);
    }

    public static class Builder implements BeerSetter, AtmosphereSetter, AmenitiesSetter, ValueSetter {

        private double beer;
        private double atmosphere;
        private double amenities;
        private double value;

        private Builder() {
            super();
        }

        @Override
        public Builder beer(double beer) {
            this.beer = beer;
            return this;
        }

        @Override
        public Builder atmosphere(double atmosphere) {
            this.atmosphere = atmosphere;
            return this;
        }

        @Override
        public Builder amenities(double amenities) {
            this.amenities = amenities;
            return this;
        }

        @Override
        public Builder value(double value) {
            this.value = value;
            return this;
        }

        public Ratings build() {
            return new Ratings(this);
        }
    }
}