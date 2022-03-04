package com.leedsbeer.service.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leedsbeer.service.domain.Venue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

public class VenueDto {

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
    private final RatingsDto ratings;
    private final Collection<String> tags;

    public VenueDto(@JsonProperty("id") int id, @JsonProperty("name") String name,
                    @JsonProperty("category") String category, @JsonProperty("url") String url,
                    @JsonProperty("timestamp") Instant timestamp, @JsonProperty("excerpt") String excerpt,
                    @JsonProperty("thumbnail") String thumbnail, @JsonProperty("latitude") double latitude,
                    @JsonProperty("longitude") double longitude, @JsonProperty("address") String address,
                    @JsonProperty("phone") String phone, @JsonProperty("twitterHandle") String twitterHandle,
                    @JsonProperty("ratings") RatingsDto ratings, @JsonProperty("tags") Collection<String> tags) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.url = url;
        this.timestamp = timestamp;
        this.excerpt = excerpt;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.phone = phone;
        this.twitterHandle = twitterHandle;
        this.ratings = ratings;
        this.tags = Collections.unmodifiableCollection(tags);
    }

    public static VenueDto from(Venue domain) {
        return new VenueDto(domain.getId(), domain.getName(), domain.getCategory(), domain.getUrl(),
                domain.getTimestamp(), domain.getExcerpt(), domain.getThumbnail(),
                domain.getLatitude(), domain.getLongitude(), domain.getAddress(),
                domain.getPhone(), domain.getTwitterHandle(), RatingsDto.from(domain.getRatings()),
                domain.getTags());
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

    public RatingsDto getRatings() {
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
}