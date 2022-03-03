package com.leedsbeer.service.impl;

import com.leedsbeer.service.domain.Venue;
import com.leedsbeer.service.domain.VenueRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.leedsbeer.service.domain.Ratings.someRatings;
import static com.leedsbeer.service.domain.Venue.aVenue;

public class JdbcVenueRepository implements VenueRepository {

    private static final String SELECT_VENUE_BY_ID = "SELECT id,name,category,url,date,excerpt,thumbnail,lat,lng,address,phone,twitter,stars_beer,stars_atmosphere,stars_amenities,stars_value,tags FROM BEER.VENUE WHERE id = ?";

    private final DataSource dataSource;

    public JdbcVenueRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Venue> findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_VENUE_BY_ID)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Venue venue = createVenueFrom(resultSet);
                        return Optional.of(venue);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new JdbcException("Unable to find venue with id=" + id, e);
        }
    }

    private Venue createVenueFrom(ResultSet resultSet) throws SQLException {
        Venue venue = aVenue()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .category(resultSet.getString("category"))
                .url(resultSet.getString("url"))
                .timestamp(resultSet.getTimestamp("date").toInstant())
                .excerpt(resultSet.getString("excerpt"))
                .thumbnail(resultSet.getString("thumbnail"))
                .latitude(resultSet.getDouble("lat"))
                .longitude(resultSet.getDouble("lng"))
                .address(resultSet.getString("address"))
                .phone(resultSet.getString("phone"))
                .twitterHandle(resultSet.getString("twitter"))
                .ratings(someRatings()
                        .beer(resultSet.getDouble("stars_beer"))
                        .atmosphere(resultSet.getDouble("stars_atmosphere"))
                        .amenities(resultSet.getDouble("stars_amenities"))
                        .value(resultSet.getDouble("stars_value"))
                        .build())
                .tags(resultSet.getString("tags").split(","))
                .build();
        return venue;
    }
}