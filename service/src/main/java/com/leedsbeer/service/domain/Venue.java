package com.leedsbeer.service.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

public class Venue {

    private final int id;
    private final String name;
    private final String category;
    private final String url;
    private final Instant timestamp;
    private final String excerpt;
    private final String thumbnail;
    private final double latitude;
    private final double longitude;
    private final String address;
    private final String phone;
    private final String twitterHandle;
    private final Ratings ratings;
    private final Collection<String> tags;

    private Venue(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.category = builder.category;
        this.url = builder.url;
        this.timestamp = builder.timestamp;
        this.excerpt = builder.excerpt;
        this.thumbnail = builder.thumbnail;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.address = builder.address;
        this.phone = builder.phone;
        this.twitterHandle = builder.twitterHandle;
        this.ratings = builder.ratings;
        this.tags = builder.tags;
    }

    public static IdSetter aVenue() {
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public Collection<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("category", category)
                .append("url", url)
                .append("timestamp", timestamp)
                .append("excerpt", excerpt)
                .append("thumbnail", thumbnail)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("address", address)
                .append("phone", phone)
                .append("twitterHandle", twitterHandle)
                .append("ratings", ratings)
                .append("tags", tags)
                .toString();
    }

    public interface IdSetter {
        NameSetter id(int id);
    }

    public interface NameSetter {
        CategorySetter name(String name);
    }

    public interface CategorySetter {
        UrlSetter category(String category);
    }

    public interface UrlSetter {
        TimestampSetter url(String url);
    }

    public interface TimestampSetter {
        ExcerptSetter timestamp(Instant timestamp);
    }

    public interface ExcerptSetter {
        ThumbnailSetter excerpt(String excerpt);
    }

    public interface ThumbnailSetter {
        LatitudeSetter thumbnail(String thumbnail);
    }

    public interface LatitudeSetter {
        LongitudeSetter latitude(double latitude);
    }

    public interface LongitudeSetter {
        AddressSetter longitude(double longitude);
    }

    public interface AddressSetter {
        PhoneSetter address(String address);
    }

    public interface PhoneSetter {
        TwitterHandleSetter phone(String phone);
    }

    public interface TwitterHandleSetter {
        RatingsSetter twitterHandle(String handle);
    }

    public interface RatingsSetter {
        TagsSetter ratings(Ratings ratings);
    }

    public interface TagsSetter {
        Builder tags(String... tags);

        Builder tags(Collection<String> tags);
    }

    public static class Builder implements IdSetter, NameSetter, CategorySetter, UrlSetter, TimestampSetter, ExcerptSetter, ThumbnailSetter, LatitudeSetter, LongitudeSetter, AddressSetter, PhoneSetter, TwitterHandleSetter, RatingsSetter, TagsSetter {

        private int id;
        private String name;
        private String category;
        private String url;
        private Instant timestamp;
        private String excerpt;
        private String thumbnail;
        private double latitude;
        private double longitude;
        private String address;
        private String phone;
        private String twitterHandle;
        private Ratings ratings;
        private Collection<String> tags;

        private Builder() {
            super();
        }

        @Override
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder category(String category) {
            this.category = category;
            return this;
        }

        @Override
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        @Override
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public Builder excerpt(String excerpt) {
            this.excerpt = excerpt;
            return this;
        }

        @Override
        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        @Override
        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        @Override
        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        @Override
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        @Override
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public Builder twitterHandle(String handle) {
            this.twitterHandle = handle;
            return this;
        }

        @Override
        public Builder ratings(Ratings ratings) {
            this.ratings = ratings;
            return this;
        }

        @Override
        public Builder tags(String... tags) {
            return tags(asList(tags));
        }

        @Override
        public Builder tags(Collection<String> tags) {
            this.tags = Collections.unmodifiableCollection(tags);
            return this;
        }

        public Venue build() {
            return new Venue(this);
        }
    }
}