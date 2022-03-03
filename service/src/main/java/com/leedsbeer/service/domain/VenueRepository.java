package com.leedsbeer.service.domain;

import java.util.Optional;

public interface VenueRepository {

    Optional<Venue> findById(int id);
}
