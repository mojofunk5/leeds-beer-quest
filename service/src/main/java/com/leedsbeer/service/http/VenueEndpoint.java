package com.leedsbeer.service.http;

import com.leedsbeer.service.domain.Venue;
import com.leedsbeer.service.domain.VenueRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class VenueEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(VenueEndpoint.class);

    private final VenueRepository venueRepository;

    private VenueEndpoint(Javalin javalin, VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
        javalin.get("/venue/{id}", this::get);
    }

    public static void createAndRegister(Javalin javalin, VenueRepository venueRepository) {
        new VenueEndpoint(javalin, venueRepository);
    }

    @OpenApi(
            path = "/venue/{id}",
            method = HttpMethod.GET,
            pathParams = {
                    @OpenApiParam(name = "id", description = "The id of the venue to retrieve")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = VenueDto.class)),
                    @OpenApiResponse(status = "404", description = "No venue with the specified id exists")
            }
    )
    public void get(Context context) {
        int id = context.pathParamAsClass("id", Integer.class).get();

        try {
            Optional<Venue> optionalVenue = venueRepository.findById(id);
            if (optionalVenue.isPresent()) {
                VenueDto venueDto = VenueDto.from(optionalVenue.get());
                context.json(venueDto);
            } else {
                context.status(HttpCode.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            logger.error("Unexpected exception whilst trying to get venue with id={}", id, e);
            context.status(HttpCode.INTERNAL_SERVER_ERROR);
        }
    }
}